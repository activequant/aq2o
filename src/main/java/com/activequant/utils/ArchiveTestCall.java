package com.activequant.utils;

import java.util.Date;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.IArchiveWriter;
import com.activequant.archive.TSContainer;
import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.exceptions.InvalidDate8Time6Input;

/**
 * Helper app to do a simple test call to the archive. Useful for setting up monitoring tests. 
 * 
 * @author ustaudinger
 *
 */
public class ArchiveTestCall {

	public static void main(String[] args) throws InvalidDate8Time6Input, Exception{
        
	    HBaseArchiveFactory fac = new HBaseArchiveFactory(args[0]);
        IArchiveReader iar = fac.getReader(TimeFrame.EOD);
        IArchiveWriter iwr = fac.getWriter(TimeFrame.EOD);
        
        TimeStamp now = new TimeStamp(new Date());
        iwr.write("TEST",now,"PX_SETTLE", Math.random());
        iwr.commit();
        
        TSContainer container = iar.getTimeSeries("TEST", "PX_SETTLE", new TimeStamp(0L));
        if(container!=null && container.timeStamps.length==0)
        	System.out.println("All ok.");        
        else{
            System.out.println("Received: " + container.timeStamps.length + " values. ");
        }
        
	}
	
}
