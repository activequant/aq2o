package com.activequant.trading;

import java.io.FileNotFoundException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.backtesting.reporting.PNLMonitor;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.timeseries.DoubleColumn;
import com.activequant.tools.streaming.PNLChangeEvent;

public class PNLMonitorTest extends TestCase {
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(PNLMonitorTest.class);
	}

	public PNLChangeEvent ce(long l, String id, Double val){		
		PNLChangeEvent p = new PNLChangeEvent(new TimeStamp(l), id, val);
		return p;
	}
	
	public void testMonitor() throws FileNotFoundException, Exception {
		// 
		PNLMonitor monitor = new PNLMonitor(null);
			
		// 
		monitor.process(ce(10L, "A", 0.0));
		monitor.process(ce(20L, "A", 1.0));
		monitor.process(ce(30L, "A", 0.0));
		monitor.process(ce(40L, "A", 2.0));
		monitor.process(ce(50L, "A", -0.2));
		
		// 
		assertEquals(2, monitor.getTsContainer().getNumColumns());
		assertEquals(5, monitor.getTsContainer().getNumRows());
		
		// 
		DoubleColumn dc = (DoubleColumn)monitor.getTsContainer().getColumn("A");
		assertNotNull(dc);
		DoubleColumn cumulatedPnl = dc.cumsum();		
		assertEquals(2.8, cumulatedPnl.get(4));
		
		// 
	}

}
