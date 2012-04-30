package com.activequant.trading;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.utils.CsvMapReader;
import com.activequant.utils.RenjinCore;
import com.activequant.utils.events.IEventListener;

public class PerformanceReportTest extends TestCase {
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(PerformanceReportTest.class);
	}

	public void testRenjin() throws FileNotFoundException, Exception {
		List<Double> settles = new ArrayList<Double>();
		
		// import the soybeans example and treat it like a performance curve. 
		new CsvMapReader().read(new IEventListener<Map<String,String>>() {			
			@Override
			public void eventFired(Map<String, String> event) {
				System.out.println(event);
			}
		}, new FileInputStream("./src/test/resources/sampledata/soybean_future_rolled.csv"));
		
		
		
		
		//
		RenjinCore rc = new RenjinCore();
		// let's take the soybeans example
		rc.put("x", settles);
		rc.execute("class(x)");
		
		//
		
		
		
		
		
		
	}

}
