package com.activequant.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveWriter;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.IMarketDataInstrumentDao;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.utils.worker.Worker;
import com.activequant.utils.worker.WorkerThread;

public class ImportL1DataCSV {

    private final ApplicationContext appContext;
    private final IDaoFactory idf;
    private final IMarketDataInstrumentDao mdiDao;
    private LinkedBlockingQueue<String> fileNameQueue = new LinkedBlockingQueue<String>();
    private String mdProvider;
    private IArchiveFactory archiveFactory;
    private TimeFrame timeFrame;

    public ImportL1DataCSV(String directory, final String mdprovider,
            String springInitFile, TimeFrame inTimeFrame) throws Exception {

        appContext = new ClassPathXmlApplicationContext(springInitFile);
        idf = (IDaoFactory) appContext.getBean("ibatisDao");
        mdiDao = idf.mdiDao();
        idf.instrumentDao();
        archiveFactory = appContext.getBean("archiveFactory",
                IArchiveFactory.class);
        this.timeFrame = inTimeFrame;
        // will recurse into directory.
        this.mdProvider = mdprovider;
        new FileTraversal() {
            public void onFile(final File f) {
                System.out.println(f);
                if (f.getName().toLowerCase().endsWith(".csv")) {
                    // parse it.
                    try {
                        fileNameQueue.add(f.getAbsolutePath());
                        // new AnonymousWorker().process(f.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.traverse(new File(directory));

        for (int i = 0; i < 100; i++) {
            fileNameQueue.add("TERMINATE");
        }

        //
        new Thread(new WorkerThread<String>(fileNameQueue,
                new AnonymousWorker())).start();
        new Thread(new WorkerThread<String>(fileNameQueue,
                new AnonymousWorker())).start();
        new Thread(new WorkerThread<String>(fileNameQueue,
                new AnonymousWorker())).start();
        new Thread(new WorkerThread<String>(fileNameQueue,
                new AnonymousWorker())).start();

    }

    class AnonymousWorker extends Worker<String> {
        private IArchiveWriter iaw = archiveFactory.getWriter(timeFrame);

        int lineCounter = 0;

        public void process(String event) {
            lineCounter = 0;
            if (event.equals("TERMINATE")) {
                setRunFlag(false);
                return;
            }
            System.out
                    .println("********* Processing file name >" + event + "<");
            File f = new File(event);
            System.out.println("********* Created file. ");
            try {
                importFile(
                        f.getAbsolutePath(),
                        f.getName()
                                .substring(
                                        0,
                                        f.getName().toLowerCase()
                                                .indexOf(".csv")).trim(),
                        mdProvider);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                // rest sleep.
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void importFile(String fileName, String providerspecificid,
                String mdprovider) throws Exception {
            System.out.println("Importing " + fileName + " / " + mdprovider
                    + " / " + providerspecificid);
            MarketDataInstrument tempMdi = mdiDao.findByProvId(mdprovider,
                    providerspecificid);
            if (tempMdi == null) {
                tempMdi = new MarketDataInstrument();
                tempMdi.setProviderSpecificId(providerspecificid);
                tempMdi.setMdProvider(mdprovider);
                mdiDao.create(tempMdi);
            }
            final MarketDataInstrument mdi = tempMdi;
            tempMdi = null;

            //
            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyyMMddHHmmss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            System.out.println("All set, ready to parse for " + mdi.getId()
                    + " /// " + System.currentTimeMillis());

            final long l1 = System.currentTimeMillis();
            final CsvMapReader cmr = new CsvMapReader();
            cmr.read(new IEventListener<Map<String, String>>() {

                @Override
                public void eventFired(Map<String, String> event) {
                    final String time = event.get("TIMESTAMP");
                    TimeStamp ts;

                    final Iterator<Entry<String, String>> it = event.entrySet()
                            .iterator();
                    try {
                        ts = new TimeStamp(sdf.parse(time));

                        while (it.hasNext()) {
                            Entry<String, String> entry = it.next();
                            String key = entry.getKey().toUpperCase();
                            if (key.equals("TIMESTAMP"))
                                continue;

                            if(key.equals("SIDE")) { 
                                int side = OrderSide.valueOf(entry.getValue()).getSide();
                                mdi.getId();
                                iaw.write(mdi.getId(), ts, key,
                                    Double.valueOf(side)); 
                                continue;
                            }
                            iaw.write(mdi.getId(), ts, key,
                                    Double.parseDouble(entry.getValue()));
                            if (lineCounter++ > 100) {
                                lineCounter = 0;
                                try {
                                    iaw.commit();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (ParseException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }, new FileInputStream(fileName));

            iaw.commit();
            final long l2 = System.currentTimeMillis();
            System.out.println("Took: " + (l2 - l1));

        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String startFolder = args[0];
        String mdprovider = args[1];
        String springFile = args[2];
        String timeFrame = args[3];
        System.out.println("Importing from " + startFolder
                + " all .csv files for " + mdprovider
                + ". Using spring configuration " + springFile
                + " and time frame " + timeFrame);
        new ImportL1DataCSV(startFolder, mdprovider, springFile,
                TimeFrame.valueOf(timeFrame));

    }
}
