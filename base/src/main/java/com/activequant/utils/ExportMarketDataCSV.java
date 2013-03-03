package com.activequant.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveReader;

/**
 * Exports market data from the archive.
 * 
 * @author GhostRider
 * 
 */
public class ExportMarketDataCSV {

    private final ApplicationContext appContext;
    private IArchiveFactory archiveFactory;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public ExportMarketDataCSV(final String mdprovider, String springInitFile, TimeFrame inTimeFrame, String mdiId, String startDate, String stopDate,
            String[] fields) throws Exception {

        appContext = new ClassPathXmlApplicationContext(springInitFile);
        archiveFactory = appContext.getBean("archiveFactory", IArchiveFactory.class);

        Date d1 = sdf.parse(startDate);
        Date d2 = sdf.parse(stopDate);
        System.out.println(d1.getTime());
        System.out.println(d2.getTime());
        //
        TimeStamp startTS = new TimeStamp(sdf.parse(startDate));
        TimeStamp stopTS = new TimeStamp(sdf.parse(stopDate));

        IArchiveReader iar = archiveFactory.getReader(inTimeFrame);

        for (String value : fields) {
            // TSContainer container = iar.getTimeSeries(mdiId, value, startTS, stopTS);
            TimeSeriesIterator iterator = iar.getTimeSeriesStream(mdiId, value, startTS, stopTS);
            while (iterator.hasNext()) {
                Tuple<TimeStamp, Double> val = iterator.next();
                System.out.println(val.getA().getNanoseconds() + "," + val.getB());
            }
        }
        //
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.println("Exporter <mdprovider> <springfile> <timeframe>" + " <mdiid> <startts> <stopts> <series keys in \" and sep by ,> ");

        String mdprovider = args[0];
        String springFile = args[1];
        String timeFrame = args[2];
        String mdiId = args[3];
        String startTs = args[4];
        String stopTs = args[5];
        String seriesKeys = args[6];

        new ExportMarketDataCSV(mdprovider, springFile, TimeFrame.valueOf(timeFrame), mdiId, startTs, stopTs, seriesKeys.split(","));

    }

}
