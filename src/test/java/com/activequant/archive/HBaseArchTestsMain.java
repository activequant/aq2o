package com.activequant.archive;

import java.io.IOException;

import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;

/**
 * not to be run without an underlying hbase, therefore not including in junit testing. 
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

	}

}
