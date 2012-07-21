package com.activequant.timeseries;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.utils.CsvMapReader;

public class TSContainerMethodsTest extends TestCase {
	TSContainer2 timeSeries;
	String[] headers = new String[] { "Ask", "Bid", "High", "Low" };
	TSContainer2 tsc = null;
	/*
	 * Create the test case
	 * 
	 * @param testName name of the test case
	 */
	public TSContainerMethodsTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		List<TypedColumn> columns = new ArrayList<TypedColumn>();
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());

		timeSeries = new TSContainer2(TSContainerTest.class.getName(),
				Arrays.asList(headers), columns);
		timeSeries.setRow(new TimeStamp(new Date(1336305521100L)),
				new Double[] { 2.0, 2.1, 2.2, 2.3 });
		timeSeries.setRow(new TimeStamp(new Date(1336305521300L)),
				new Double[] { 4.0, 4.1, 4.2, 4.3 });
		timeSeries.setRow(new TimeStamp(new Date(1336305521000L)),
				new Double[] { 1.0, 1.1, 1.2, 1.3 });
		timeSeries.setRow(new TimeStamp(new Date(1336305521200L)),
				new Double[] { 3.0, 3.1, 3.2, 3.3 });
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TSContainerMethodsTest.class);
	}

	public void test() {
		TSContainer2 in = new TSContainer2("Test",
				Arrays.asList(new String[] { "A" }),
				Arrays.asList(new TypedColumn[] { new DoubleColumn() }));
		in.setRow(new TimeStamp(5L), 1.0);
		in.setRow(new TimeStamp(10L), 2.0);
		in.setRow(new TimeStamp(15L), 3.0);
		in.setRow(new TimeStamp(20L), 4.0);
		in.setRow(new TimeStamp(25L), 5.0);
		in.setRow(new TimeStamp(30L), 6.0);

		assertEquals(1.0, in.getRow(new TimeStamp(5L))[0]);
		assertEquals(2.0, in.getRow(new TimeStamp(10L))[0]);

		TSContainerMethods tcm = new TSContainerMethods();
		TSContainer2 resampled1 = tcm.resample(in, 1L);
		assertEquals(in.getNumRows(), resampled1.getNumRows());
	}

	public void testReturns() {
		TSContainerMethods tcm = new TSContainerMethods();
		TSContainer2 tsc = tcm.returns(timeSeries);

		assertNull(tsc.getColumn("Ask").get(0));
		assertEquals((2.0 - 1.0) / 1.0, tsc.getColumn("Ask").get(1));
		assertEquals((3.0 - 2.0) / 2.0, tsc.getColumn("Ask").get(2));
		assertEquals((3.1 - 2.1) / 2.1, tsc.getColumn("Bid").get(2));
		assertEquals((4.2 - 3.2) / 3.2, tsc.getColumn("High").get(3));
		assertEquals((4.3 - 3.3) / 3.3, tsc.getColumn("Low").get(3));
	}

	public void testMean() {
		TSContainerMethods tcm = new TSContainerMethods();
		Double[] mean = tcm.mean(timeSeries);

		assertEquals(
				((2.0 - 1.0) / 1.0 + (3.0 - 2.0) / 2.0 + (4.0 - 3.0) / 3.0) / 3,
				mean[0]);

		assertEquals(
				((2.3 - 1.3) / 1.3 + (3.3 - 2.3) / 2.3 + (4.3 - 3.3) / 3.3) / 3,
				mean[3]);
	}
	
	public void testStd() {
		TSContainerMethods tcm = new TSContainerMethods();
		Double[] std = tcm.std(timeSeries);

		assertEquals(0.283278862, std[0], 0.000000001);
	}

	@SuppressWarnings("rawtypes")
	public void testMaxDrawdown() {
		List<TypedColumn> columns = new ArrayList<TypedColumn>();
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());

		TSContainer2 tsc = new TSContainer2(TSContainerTest.class.getName(),
				Arrays.asList(headers), columns);

		DoubleColumn testData = new DoubleColumn();

		// this test data is taken from matlab
		// http://www.mathworks.com/help/toolbox/finance/bqxcira-1.html#bqxcira-3
		testData.clear();
		testData.add(1.00000);
		testData.add(1.01680);
		testData.add(1.00600);
		testData.add(1.00940);
		testData.add(0.99060);
		testData.add(0.95165);
		testData.add(0.95500);
		testData.add(0.98522);
		testData.add(0.99530);
		testData.add(0.99127);
		testData.add(0.99060);
		testData.add(1.01280);
		testData.add(0.99396);
		testData.add(0.99328);
		testData.add(0.95097);
		testData.add(0.89993);
		testData.add(0.90934);
		testData.add(0.84822);
		testData.add(0.88784);
		testData.add(0.92881);
		testData.add(0.90463);
		testData.add(0.89053);
		testData.add(0.88046);
		testData.add(0.87777);
		testData.add(0.92478);
		testData.add(0.97045);
		testData.add(0.97985);
		testData.add(0.97246);
		testData.add(0.98187);
		testData.add(0.98522);
		testData.add(1.01280);
		testData.add(1.02150);
		testData.add(1.06180);
		testData.add(1.07450);
		testData.add(1.09200);
		testData.add(1.08870);
		testData.add(1.06380);
		testData.add(1.06510);
		testData.add(1.08260);
		testData.add(1.06720);
		testData.add(1.07920);
		testData.add(1.08530);
		testData.add(1.09740);
		testData.add(1.12290);
		testData.add(1.15040);
		testData.add(1.14040);
		testData.add(1.15780);
		testData.add(1.14370);
		testData.add(1.13230);
		testData.add(1.15580);
		testData.add(1.16520);
		testData.add(1.18330);
		testData.add(1.18400);
		testData.add(1.18600);
		testData.add(1.16320);
		testData.add(1.19070);
		testData.add(1.19680);
		testData.add(1.21630);
		testData.add(1.22230);
		testData.add(1.22430);
		testData.add(1.23440);

		List<TypedColumn> l = new ArrayList<TypedColumn>();
		l.add((DoubleColumn) testData.clone());
		l.add((DoubleColumn) testData.clone());
		l.add((DoubleColumn) testData.clone());
		l.add((DoubleColumn) testData.clone());
		
		tsc.setColumns(l);
		
		TSContainerMethods tcm = new TSContainerMethods();
		Double[] drawdown = tcm.maxDrawdown(tsc);

		for(int i=0; i<tsc.getNumColumns(); i++) {
			assertEquals(0.1658, drawdown[i], 0.00001);
		}
	}
	
	public void testMaxRecovery() {
		TSContainer2 in = new TSContainer2("Test",
				Arrays.asList(new String[] { "A" }),
				Arrays.asList(new TypedColumn[] { new DoubleColumn() }));
		in.setRow(new TimeStamp(5L), 6.0);
		in.setRow(new TimeStamp(10L), 5.0);
		in.setRow(new TimeStamp(15L), 4.0);
		in.setRow(new TimeStamp(20L), 3.0);
		in.setRow(new TimeStamp(25L), 2.0);
		in.setRow(new TimeStamp(30L), 1.0);

		TSContainerMethods tcm = new TSContainerMethods();

		assertEquals((Integer)5, tcm.maxRecoveryTime(in)[0]);
	}

	public void testAverageProfitPerSlot() throws Exception {
		final TimeStamp from = null;
		List<TypedColumn> columns = new ArrayList<TypedColumn>();
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());
		String[] headers = new String[] { "VOLUME", "OPEN", "CLOSE", "MIN", "MAX" };
		

		tsc = new TSContainer2(TSContainerTest.class.getName(),
				Arrays.asList(headers), columns);

		final SimpleDateFormat sdf = new SimpleDateFormat(
				"MM/dd/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		final CsvMapReader cmr = new CsvMapReader();

		IEventListener el = new IEventListener<Map<String, String>>() {

			@SuppressWarnings("unchecked")
			@Override
			public void eventFired(Map<String, String> event) {
				final String date = event.get("DATE");
				String time = event.get("TIME");
				if(time==null)time = "00:00:00.000";
				final String dateTime = date + " " + time;
				final Iterator<Entry<String, String>> it = event.entrySet()
						.iterator();
				TimeStamp ts;
				try {
					if(dateTime.indexOf("-")!=-1)
						ts = new TimeStamp(sdf.parse(dateTime));
					else
						ts = new TimeStamp(sdf.parse(dateTime));

					while (it.hasNext()) {
						Entry<String, String> entry = it.next();
						String key = entry.getKey().toUpperCase();
						if (key.equals("DATE"))
							continue;
						if (key.equals("TIME"))
							continue;

						tsc.setValue(key, ts, Double.parseDouble(entry.getValue()));
					}
				} catch (ParseException e1) {
					e1.printStackTrace();
				} 
			}
		};
		
		cmr.read(el, new FileInputStream("./src/test/resources/sampledata/Q62_60_2000.csv"));

		assertEquals(2000,tsc.getNumRows());
		
		TSContainerMethods tcm = new TSContainerMethods();

		// assertEquals((Double)51.6333, tcm.averageProfitPerSlot(tsc, new TimeStamp(1336760881000000000L), TimeFrame.MINUTES_5)[0], 0.0001);
	}
}
