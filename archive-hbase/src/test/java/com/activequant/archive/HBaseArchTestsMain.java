package com.activequant.archive;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveReader;
import com.activequant.interfaces.archive.IArchiveWriter;

/**
 * not to be run without an underlying hbase, therefore not including in junit testing.
 *  
 * @author ustaudinger
 *
 */
public class HBaseArchTestsMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	    HBaseArchiveFactory fac = new HBaseArchiveFactory("127.0.0.1");
        IArchiveReader iar = fac.getReader(TimeFrame.EOD);
        IArchiveWriter iwr = fac.getWriter(TimeFrame.EOD);
             
        System.out.println("About to write. ");
        TimeStamp now = new TimeStamp();
        iwr.write("A", now, new Tuple<String, Double>("ABCD", 1.0));
        iwr.commit();
        System.out.println("written.");
        // get that value
        TSContainer tsc = iar.getTimeSeries("A", "ABCD", new TimeStamp(0L));
        System.out.println(tsc.timeStamps.length);
        
        iwr.delete("A");
        iwr.commit();

        iwr.write("A", now, new Tuple<String, Double>("ABCD", 2.0));
        iwr.commit();
        tsc = iar.getTimeSeries("A", "ABCD", new TimeStamp(0L));
        System.out.println(tsc.timeStamps.length);
        System.out.println(tsc.values[0]);
        
        iwr.delete("A");
        iwr.commit();
        
        
        
        // 
        iar = fac.getReader(TimeFrame.RAW);
        iwr = fac.getWriter(TimeFrame.RAW);
        iwr.write("TZTEST", new TimeStamp(0L), new Tuple<String, Double>("A",1.0));
        
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.YEAR, 2003);
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 15);
        cal.set(Calendar.MILLISECOND, 10);
                
        iwr.write("TZTEST", new TimeStamp(cal.getTime()), new Tuple<String, Double>("A",13.0));        
        iwr.commit();
        tsc = iar.getTimeSeries("TZTEST", "A", new TimeStamp(0L));
        
        for(int i=0;i<tsc.timeStamps.length;i++){
        	System.out.println("I " + i);
        	System.out.println(tsc.timeStamps[i].getDate());
            System.out.println(tsc.timeStamps[i].getMilliseconds());
            System.out.println(tsc.values[i]);
        }
        
        // 
        
        
        

	}

}
