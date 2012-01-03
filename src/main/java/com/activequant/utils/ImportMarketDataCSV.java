package com.activequant.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveWriter;
import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.RollingSchedule;
import com.activequant.domainmodel.SuperInstrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.utils.worker.Worker;
import com.activequant.utils.worker.WorkerThread;

/**
 * Imports recursively all ".csv" file (case sensitive) from a start folder.
 * Requires that the user specifies a market data provider name.
 * 
 * Date and time, <arbitrary amount of fields> date and time have to be in the
 * format "yyyyMMdd HH:mm:ss.SSS". The file must have a header and must be comma
 * separated. Decimal separator must be a ".". Fields must only contain double
 * values. Filename of csv file is used as market data instrument provider
 * specific name.
 * 
 * 
 * @author ustaudinger
 * 
 */
public class ImportMarketDataCSV {

    private final ApplicationContext appContext;
    private final IDaoFactory idf;
    private final IMarketDataInstrumentDao mdiDao;
    private final IInstrumentDao idao;
    private LinkedBlockingQueue<String> fileNameQueue = new LinkedBlockingQueue<String>();
    private String mdProvider;

    public ImportMarketDataCSV(String directory, final String mdprovider, String springInitFile) throws Exception {

        appContext = new ClassPathXmlApplicationContext(springInitFile);
        idf = (IDaoFactory) appContext.getBean("ibatisDao");
        mdiDao = idf.mdiDao();
        idao = idf.instrumentDao();

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
        new Thread(new WorkerThread<String>(fileNameQueue, new AnonymousWorker())).start();
        new Thread(new WorkerThread<String>(fileNameQueue, new AnonymousWorker())).start();
        new Thread(new WorkerThread<String>(fileNameQueue, new AnonymousWorker())).start();
        new Thread(new WorkerThread<String>(fileNameQueue, new AnonymousWorker())).start();

    }

    class AnonymousWorker extends Worker<String> {
        IArchiveWriter iaw = new HBaseArchiveFactory("localhost").getWriter(TimeFrame.EOD);

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
            Date8Time6Parser d8t6p = new Date8Time6Parser();
            MarketDataInstrument mdi = mdiDao.findByProvId(mdprovider, providerspecificid);
            if (mdi == null) {
                mdi = new MarketDataInstrument();
                mdi.setProviderSpecificId(providerspecificid);
                mdi.setMdProvider(mdprovider);
                mdiDao.create(mdi);
            }

            SuperInstrument si = null;
            RollingSchedule rs = null;
            // check if we have a corresponding super instrument for later
            // rolling schedule updating.
            if (mdi.getInstrumentId() != null) {
                Instrument i = idao.load(mdi.getInstrumentId());
                if (i != null) {
                    if (i instanceof SuperInstrument) {
                        si = (SuperInstrument) i;
                        rs = si.rollingSchedule();
                        rs.clear();
                    }
                }
            }

            //
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            System.out.println("All set, ready to parse for " + mdi.getId() + " /// " + System.currentTimeMillis());
            //
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String l = br.readLine();
            long l1 = System.currentTimeMillis();
            List<String> fieldNames = new ArrayList<String>();
            fieldNames.add("DATE");
            fieldNames.add("OPEN");
            fieldNames.add("HIGH");
            fieldNames.add("LOW");
            fieldNames.add("CLOSE");
            fieldNames.add("X1");
            fieldNames.add("X2");
            fieldNames.add("X3");
            fieldNames.add("X4");
            fieldNames.add("USEDEXPIRY");
            fieldNames.add("UNADJCLOSE");

            String currentExpiry = "";
            int lineCounter = 0;
            while (l != null) {

                String[] splitted = l.split(",");
                if (splitted.length != 11) {
                    l = br.readLine();
                    continue;
                }
                String timeStampS = splitted[0];
                long ms = sdf.parse(timeStampS).getTime();

                for (int i = 0; i < splitted.length; i++) {
                    String fieldName = fieldNames.get(i);
                    if (splitted[i].length() > 0) {
                        try {
                            // extract expiry.
                            if (fieldName.equals("USEDEXPIRY")) {
                                String expiry = splitted[i];
                                if (!expiry.equals(currentExpiry) && rs != null) {
                                    currentExpiry = expiry;
                                    rs.put(Long.parseLong(splitted[0]), currentExpiry);
                                }
                            }
                            // write to archive
                            iaw.write(mdi.getId(), d8t6p.fromMilliseconds(ms), fieldName.toUpperCase(),
                                    Double.parseDouble(splitted[i]));
                            if (lineCounter++ > 100) {
                                lineCounter = 0;
                                iaw.commit();
                            }
                        } catch (Exception e) {
                            System.out.println(l);
                            e.printStackTrace();
                        }
                    }

                }

                l = br.readLine();
            }
            System.out.println("About to commit data. ");
            iaw.commit();
            long l2 = System.currentTimeMillis();
            System.out.println("Took: " + (l2 - l1));

            // condense the rolling schedule
            if (rs != null) {
                String validInstrument = null;
                List<Long> rollingDates = new ArrayList<Long>();
                List<String> validInstruments = new ArrayList<String>();
                Iterator<Entry<Long, String>> it = rs.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<Long, String> entry = it.next();
                    Long key = entry.getKey();
                    String value = entry.getValue();
                    if (value != validInstrument) {
                        validInstrument = value;
                        rollingDates.add(key);
                        validInstruments.add(value);
                    }
                }

                // update the si in case it was not null ...
                if (si != null && rs != null) {
                    si.setRollDates(rollingDates.toArray(new Long[] {}));
                    si.setValidInstrument(validInstruments.toArray(new String[] {}));
                    idao.update(si);
                }
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String startFolder = args[0];
        String mdprovider = args[1];
        String springFile = args[2];
        System.out.println("Importing from " + startFolder + " all .csv files for " + mdprovider
                + ". Using spring configuration " + springFile + ".");
        new ImportMarketDataCSV(startFolder, mdprovider, springFile);

    }

}
