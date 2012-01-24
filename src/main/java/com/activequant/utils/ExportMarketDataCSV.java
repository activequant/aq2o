package com.activequant.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.IArchiveReader;
import com.activequant.archive.TSContainer;
import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.servicelayer.matlab.MatlabServiceFacade;
import com.activequant.servicelayer.matlab.TimeSeriesContainer;

/**
 * Exports market data from the archive.
 * 
 * @author ustaudinger
 * 
 */
public class ExportMarketDataCSV {

    private final ApplicationContext appContext;
    private IArchiveFactory archiveFactory;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public ExportMarketDataCSV(final String mdprovider, String springInitFile, TimeFrame inTimeFrame, String mdiId, String value,
            String startDate, String stopDate) throws Exception {

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
        TSContainer container = iar.getTimeSeries(mdiId, value, startTS, stopTS);
        TimeSeriesIterator iterator = iar.getTimeSeriesStream(mdiId, value, startTS, stopTS);
        while(iterator.hasNext())
        {
            Tuple<TimeStamp, Double> val = iterator.next();
            System.out.println(val.getA().getNanoseconds() +","+val.getB());
        }
        
        // 
        MatlabServiceFacade msf = new MatlabServiceFacade("ahlinux1");
        TimeSeriesContainer ts = msf.fetchTSData(TimeFrame.EOD, "BBGT_BOK11 COMDTY", "PX_SETTLE", 0.0, new HashMap());
        System.out.println(ts);
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String mdprovider = args[0];
        String springFile = args[1];
        String timeFrame = args[2];
        String mdiId = args[3];
        String seriesKey = args[4];
        String startTs = args[5];
        String stopTs = args[6];

        new ExportMarketDataCSV(mdprovider, springFile, TimeFrame.valueOf(timeFrame), mdiId, seriesKey, startTs, stopTs);

    }

}
