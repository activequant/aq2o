package com.activequant.utils;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.TSContainer;
import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.exceptions.InvalidDate8Time6Input;

/**
 * Helper app to do a simple test call to the archive. Useful for setting up monitoring tests. 
 * 
 * @author ustaudinger
 *
 */
public class ArchiveTestCall {

	public static void main(String[] args) throws InvalidDate8Time6Input, Exception{
        IArchiveReader iar = new HBaseArchiveFactory(args[0]).getReader(TimeFrame.EOD);
        TSContainer container = iar.getTimeSeries("TEST_SERIES", "No-Series", new Date8Time6(2011123123595959.999));
        if(container!=null && container.timeStamps.length==0)
        	System.out.println("All ok.");
	}
	
}
