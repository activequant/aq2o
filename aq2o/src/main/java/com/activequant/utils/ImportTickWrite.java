package com.activequant.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveWriter;
import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.utils.worker.Worker;
import com.activequant.utils.worker.WorkerThread;

public class ImportTickWrite {
    ApplicationContext appContext = new ClassPathXmlApplicationContext("initdblive.xml");
    IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
    private LinkedBlockingQueue<String> fileNameQueue = new LinkedBlockingQueue<String>();
    IMarketDataInstrumentDao mdidao = idf.mdiDao();
    String mdProvider;

    public ImportTickWrite(String directory, final String mdprovider) throws Exception {
        // will recurse into directory.
        this.mdProvider = mdprovider;
        new FileTraversal() {
            public void onFile(final File f) {
                System.out.println(f);
                if (f.getName().endsWith(".csv")) {
                    // parse it.
                    try {
                        fileNameQueue.add(f.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.traverse(new File(directory));
        new Thread(new WorkerThread<String>(fileNameQueue, new AnonymousWorker())).start();
        /*
         * new Thread(new WorkerThread<String>(fileNameQueue, new
         * AnonymousWorker())).start(); new Thread(new
         * WorkerThread<String>(fileNameQueue, new AnonymousWorker())).start();
         * new Thread(new WorkerThread<String>(fileNameQueue, new
         * AnonymousWorker())).start();
         */
    }

    class AnonymousWorker extends Worker<String> {
        IArchiveWriter iaw = new HBaseArchiveFactory().getWriter(TimeFrame.MINUTES_5);

        public void process(String event) {
            if (event.equals("TERMINATE")) {
                setRunFlag(false);
                return;
            }
            System.out.println("********* Processing file name >" + event + "<");
            File f = new File(event);
            System.out.println("********* Created file. ");
            try {
                importFile(f.getAbsolutePath(), f.getName().substring(0, f.getName().toLowerCase().indexOf(".csv"))
                        .trim(), mdProvider);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void importFile(String fileName, String providerspecificid, String mdprovider) throws Exception {
            System.out.println("Importing " + fileName + " / " + mdprovider + " / " + providerspecificid);

            MarketDataInstrument mdi = mdidao.findByProvId(mdprovider, providerspecificid);
            if (mdi == null) {
                mdi = new MarketDataInstrument();
                mdi.setProviderSpecificId(providerspecificid);
                mdi.setMdProvider(mdprovider);
                mdidao.create(mdi);
            }

            //
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            System.out.println("All set, ready to parse for " + mdi.getId() + " /// " + System.currentTimeMillis());
            //
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String l = br.readLine();
            long l1 = System.currentTimeMillis();
            List<String> fieldNames = new ArrayList<String>();
            int lineCounter = 0;
            Date8Time6Parser d8t6p = new Date8Time6Parser();
            while (l != null) {
                if (l.startsWith("Date")) {
                    // parse header.
                    String[] splitted = l.split(",");
                    for (String s : splitted) {
                        fieldNames.add(s);
                    }
                } else {
                    String[] splitted = l.split(",");
                    String timeStampS = splitted[0] + " " + splitted[1];
                    long ms = sdf.parse(timeStampS).getTime();
                    Date8Time6 d8t6 = d8t6p.fromMilliseconds(ms);

                    for (int i = 0; i < splitted.length; i++) {
                        String fieldName = fieldNames.get(i);
                        if (splitted[i].length() > 0) {
                            try {
                                iaw.write(mdi.getId(), d8t6, fieldName.toUpperCase(), Double.parseDouble(splitted[i]));
                                if (lineCounter++ > 100) {
                                    lineCounter = 0;
                                    iaw.commit();
                                }
                            } catch (Exception e) {
                            }
                        }
                    }

                    // will output in GMT!!!
                    // System.out.println();
                }
                l = br.readLine();
            }

            iaw.commit();
            long l2 = System.currentTimeMillis();
            System.out.println("Took: " + (l2 - l1));
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String startFolder = args[0];
        String mdprovider = "TICKDATA";
        new ImportTickWrite(startFolder, mdprovider);

    }

}
