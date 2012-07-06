package com.activequant.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.IArchiveWriter;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.utils.events.IEventListener;
import com.activequant.utils.worker.Worker;
import com.activequant.utils.worker.WorkerThread;

/**
 * Pi trading sells one-minute bar data. 
 * 
 * @author anon.
 * 
 */
public class ImportPiTradingData {

    private final ApplicationContext appContext;
    private final IDaoFactory idf;
    private final IMarketDataInstrumentDao mdiDao;
    private final IInstrumentDao idao;
    private LinkedBlockingQueue<String> fileNameQueue = new LinkedBlockingQueue<String>();
    private String mdProvider;
    private IArchiveFactory archiveFactory;
    private TimeFrame timeFrame;

    public ImportPiTradingData(String directory, final String mdprovider, String springInitFile, TimeFrame inTimeFrame) throws Exception {

        appContext = new ClassPathXmlApplicationContext(springInitFile);
        idf = (IDaoFactory) appContext.getBean("ibatisDao");
        mdiDao = idf.mdiDao();
        idao = idf.instrumentDao();
        archiveFactory = appContext.getBean("archiveFactory", IArchiveFactory.class);
        this.timeFrame = inTimeFrame;
        // will recurse into directory.
        this.mdProvider = mdprovider;
        new FileTraversal() {
        	@Override
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
        new Thread(new WorkerThread<String>(fileNameQueue, new AnonymousWorker())).start();
        new Thread(new WorkerThread<String>(fileNameQueue, new AnonymousWorker())).start();
        //new Thread(new WorkerThread<String>(fileNameQueue, new AnonymousWorker())).start();
        //new Thread(new WorkerThread<String>(fileNameQueue, new AnonymousWorker())).start();

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
            System.out.println("********* Processing file name >" + event + "<");
            File f = new File(event);
            System.out.println("********* Created file. ");
            try {
                importFile(f.getAbsolutePath(), f.getName().substring(0, f.getName().toLowerCase().indexOf(".csv")).trim(), mdProvider);
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

        public void importFile(String fileName, String providerspecificid, String mdprovider) throws Exception {
            System.out.println("Importing " + fileName + " / " + mdprovider + " / " + providerspecificid);            
            MarketDataInstrument tempMdi = mdiDao.findByProvId(mdprovider, providerspecificid);
            if (tempMdi == null) {
                tempMdi = new MarketDataInstrument();
                tempMdi.setProviderSpecificId(providerspecificid);
                tempMdi.setMdProvider(mdprovider);
                mdiDao.create(tempMdi);
            }
            // 
            final MarketDataInstrument mdi = tempMdi;
            tempMdi = null;

            //
            final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HHmm");
            // explicitly set it to 
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            System.out.println("All set, ready to parse for " + mdi.getId() + " /// " + System.currentTimeMillis());

            final long l1 = System.currentTimeMillis();
            final CsvMapReader cmr = new CsvMapReader();
            // cmr.setHeader(new String[] { "TIMESTAMP", "TYPE", "PRICE", "QUANTITY", "IND"});
            cmr.read(new IEventListener<Map<String, String>>() {

                @Override
                public void eventFired(Map<String, String> event) {
                	// these magic strings should leave at one point. 
                    final String date = event.get("DATE");
                    final String time = event.get("TIME");
                    final String open = event.get("OPEN");
                    final String high= event.get("HIGH");
                    final String low = event.get("LOW");
                    final String close = event.get("CLOSE");
                    final String volume = event.get("VOLUME");
                    
                    
                    final String timestamp = date + " " + time;  
                    
                    
                    try {
                        TimeStamp ts = new TimeStamp(sdf.parse(timestamp));                        
                        iaw.write(mdi.getId(), ts, "OPEN", Double.parseDouble(open));
                        iaw.write(mdi.getId(), ts, "HIGH", Double.parseDouble(high));
                        iaw.write(mdi.getId(), ts, "LOW", Double.parseDouble(low));
                        iaw.write(mdi.getId(), ts, "CLOSE", Double.parseDouble(close));
                        iaw.write(mdi.getId(), ts, "VOL", Double.parseDouble(volume));
                                               
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    if (lineCounter++ > 100) {
                        lineCounter = 0;
                        try {
                        	System.out.print(".");
                            iaw.commit();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
        System.out.println("Importing from " + startFolder + " all .csv files for " + mdprovider + ". Using spring configuration " + springFile
                + " and time frame " + timeFrame);
        new ImportPiTradingData(startFolder, mdprovider, springFile, TimeFrame.valueOf(timeFrame));

    }

}
