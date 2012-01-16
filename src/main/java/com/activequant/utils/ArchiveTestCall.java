package com.activequant.utils;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.TSContainer;
import com.activequant.archive.hbase.HBaseArchiveFactory;
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
        TSContainer container = iar.getTimeSeries("TEST_SERIES", "No-Series", new UniqueTimeStampGenerator().now());
        if(container!=null && container.timeStamps.length==0)
        	System.out.println("All ok.");
	}
	
}
