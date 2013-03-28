package com.activequant.utils;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.archive.basic.AQMSArchiveReader;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveReader;

public class AQMSArchReaderTest extends TestCase {
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AQMSArchReaderTest.class);
	}

	public void testChains() throws Exception{
		IArchiveReader r = new AQMSArchiveReader("http://27.50.89.125:44444/csv/", TimeFrame.MINUTES_15);
		Tuple<TimeStamp, Map<String, Double>> t = r.getMultiValueStream("TT.Eurex.FGBL062013.FGBL",new TimeStamp(), new TimeStamp()).next();
		assertNotNull(t);
		System.out.println(t.getB());
		
	}
}
