package com.activequant;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.archive.TSContainer;
import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveReader;
import com.activequant.interfaces.archive.IArchiveWriter;
import com.activequant.server.LocalHBaseCluster;


/**
 * 
 * @author GhostRider
 *
 */
public class HBaseServerTest  extends TestCase {
	
	private LocalHBaseCluster localCluster; 
	
	public HBaseServerTest() throws Exception{
		localCluster = new LocalHBaseCluster();
		localCluster.start();
	}
	
	
	
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(HBaseServerTest.class);
    }
    public void testBasicReadWrite(){
    	
    }
    
}