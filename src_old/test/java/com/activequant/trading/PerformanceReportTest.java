package com.activequant.trading;

import java.io.FileNotFoundException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.archive.TSContainer;
import com.activequant.archive.csv.CsvArchiveReaderFormat1;
import com.activequant.utils.Date8Time6Parser;
import com.activequant.utils.RenjinCore;

public class PerformanceReportTest extends TestCase {
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(PerformanceReportTest.class);
	}

	public void testRenjin() throws FileNotFoundException, Exception {
		
		CsvArchiveReaderFormat1 c = new CsvArchiveReaderFormat1("./src/test/resources/sampledata/soybean_future_rolled.csv");
		TSContainer tsc = c.getTimeSeries("", "PX_SETTLE", new Date8Time6Parser().getTimeStamp(20100101000000.0));
		
		assertNotNull(tsc.values);
		assertEquals(582, tsc.values.length);
		
		
		//
		RenjinCore rc = new RenjinCore();
		// let's take the soybeans example
		rc.put("x", tsc.values);
		rc.execute("sma = mean(x)");
		
		System.out.println(rc.get("sma"));
		
		//
		
		
		
		
		
		
	}

}
