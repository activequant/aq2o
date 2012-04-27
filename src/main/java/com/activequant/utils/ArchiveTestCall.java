package com.activequant.utils;

import java.util.Date;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.IArchiveWriter;
import com.activequant.archive.TSContainer;
import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;

/**
 * Helper app to do a simple test call to the archive. Useful for setting up monitoring tests. 
 * 
 * @author ustaudinger
 *
 */
public class ArchiveTestCall {

	public static void main(String[] args) throws Exception{
        
	    HBaseArchiveFactory fac = new HBaseArchiveFactory(args[0]);
        IArchiveReader iar = fac.getReader(TimeFrame.EOD);
        IArchiveWriter iwr = fac.getWriter(TimeFrame.EOD);
        System.out.println("writer and reader received. ");
        TimeStamp now = new TimeStamp(new Date());
        double value = Math.random(); 
        iwr.write("TEST",now,"PX_SETTLE", value);
        iwr.commit();
        
        TSContainer container = iar.getTimeSeries("TEST", "PX_SETTLE", now);
        if(container!=null && container.timeStamps.length==1 && container.values[0] == value)
        	System.out.println("All ok.");        
        else{
            System.out.println("Received: " + container.timeStamps.length + " values. ");
        }
        
        container = fac.getReader(TimeFrame.MINUTES_5).getTimeSeries("TICKDATA_AUH12", "CLOSE", new TimeStamp(0L));
        System.out.println(container.timeStamps[0].getDate());
        System.out.println(container.timeStamps[1].getDate());
        System.out.println(container.timeStamps.length);
        
	}
	
}
