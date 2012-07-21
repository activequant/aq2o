package com.activequant.timeseries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.timeseries.DoubleColumn;
import com.activequant.timeseries.TSContainer2;
import com.activequant.timeseries.TypedColumn;

/**
 * Unit test for time series TSContainer.
 * 
 * @author BrownChipmunk
 */
@SuppressWarnings("rawtypes")
public class TSContainerTest extends TestCase {
	TSContainer2 timeSeries;
	String[] headers = new String[] { "Ask", "Bid", "High", "Low" };

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

	@Test
	public void testGetSeriesId() {
		assertNotNull("SeriesId cannot be null", timeSeries.getSeriesId());
	}

	@Test
	public void testGetNumRows() {
		assertEquals(4, timeSeries.getNumRows());
		List<TimeStamp> tsl = timeSeries.getTimeStamps();
		assertEquals(timeSeries.getNumRows(), tsl.size());
	}

	@Test
	public void testAddColumn() {
		int sizeBefore = timeSeries.getNumColumns();
		timeSeries.addColumn("SSSS", new DoubleColumn());

		int sizeAfter = timeSeries.getNumColumns();

		List l = timeSeries.getColumn("SSSS");
		List ts = timeSeries.getTimeStamps();

		assertNotNull(timeSeries.getColumn("SSSS"));
		assertEquals(sizeAfter - 1, sizeBefore);
		assertNotNull(l);
		assertNotNull(ts);
		assertEquals(l.size(), ts.size());
	}

	@Test
	public void testAdd() {
		timeSeries.setRow(new TimeStamp(new Date(1336305521250L)),
				new Double[] { 3.0, 3.1, 3.2, 3.3 });
	}

	@Test
	public void testDeleteColumn() {
		timeSeries.addColumn("SSSS", new DoubleColumn());

		int sizeBefore = timeSeries.getNumColumns();
		timeSeries.deleteColumn("SSSS");
		int sizeAfter = timeSeries.getNumColumns();

		assertNull(timeSeries.getColumn("SSSS"));
		assertEquals(sizeAfter, sizeBefore - 1);
	}

	@Test
	public void testDeleteFromTo() {
		timeSeries.delete(new TimeStamp(new Date(1336305521000L)),
				new TimeStamp(new Date(1336305521200L)));

		assertEquals(1, timeSeries.getNumRows());
		assertEquals(1336305521300L, timeSeries.getTime(0).getMilliseconds());
		assertEquals(4.0,
				timeSeries.getRow(new TimeStamp(new Date(1336305521300L)))[0]);
	}

	@Test
	public void testGetIndexBeforeOrEqual() {
		System.out.println(timeSeries);
		assertEquals(2, timeSeries.getIndexBeforeOrEqual(new TimeStamp(
				new Date(1336305521200L))));
		assertEquals(2, timeSeries.getIndexBeforeOrEqual(new TimeStamp(
				new Date(1336305521250L))));
	}


	@Test
	public void testGetIndexBefore() {
		System.out.println(timeSeries);
		assertEquals(1, timeSeries.getIndexBefore(new TimeStamp(
				new Date(1336305521200L))));
		assertEquals(2, timeSeries.getIndexBefore(new TimeStamp(
				new Date(1336305521250L))));
	}
	
	@Test
	public void testGetIndexAfter() {
		assertEquals(3, timeSeries.getIndexAfter(new TimeStamp(new Date(
				1336305521200L))));
		assertEquals(3, timeSeries.getIndexAfter(new TimeStamp(new Date(
				1336305521250L))));
	}

	@Test
	public void testGetMax() {
		assertEquals(4.0, timeSeries.getMax("Ask"));

		timeSeries.addColumn("NotComparable", new DoubleColumn());
		assertNull(timeSeries.getMax("NotComparable"));
	}

	@Test
	public void testGetMin() {
		assertEquals(1.1, timeSeries.getMin("Bid"));

		timeSeries.addColumn("NotComparable", new DoubleColumn());
		assertNull(timeSeries.getMax("NotComparable"));
	}

	@Test
	public void testGetMinTimestamp() {
		assertEquals(new TimeStamp(new Date(1336305521000L)),
				timeSeries.getMinTimestamp());
	}

	@Test
	public void testGetMaxTimestamp() {
		assertEquals(new TimeStamp(new Date(1336305521300L)),
				timeSeries.getMaxTimestamp());
	}

	@Test
	public void testSetWindowSize() throws InterruptedException {
		timeSeries.delete(new TimeStamp(new Date(1336305521000L)),
				new TimeStamp(new Date(1336305521300L)));

		assertEquals(0, timeSeries.getNumRows());

		TimeStamp ts1 = new TimeStamp(new Date());
		Thread.sleep(1);
		TimeStamp ts2 = new TimeStamp(new Date());
		Thread.sleep(1);

		timeSeries.setMaxWindow(10);
		timeSeries.setRow(ts1, 1.0, 1.1, 1.2, 1.3);
		timeSeries.setRow(ts2, 2.0, 2.1, 2.2, 2.3);

		for (int i = 0; i < 7; i++) {
			timeSeries.setRow(new TimeStamp(new Date()), 2.0 + i, 2.1 + i,
					2.2 + i, 2.3 + i);
			Thread.sleep(1);
		}

		// 10th row
		TimeStamp ts3 = new TimeStamp(new Date());
		Thread.sleep(1);
		timeSeries.setRow(ts3, 8.0, 8.1, 8.2, 8.3);

		// 11th Row -- everything should be shifted left
		timeSeries.setRow(new TimeStamp(new Date()), 9.0, 9.1, 9.2, 9.3);

		assertEquals(10, timeSeries.getNumRows());
		assertEquals(ts2, timeSeries.getMinTimestamp());

		// prepare for shift to right
		TimeStamp ts4 = new TimeStamp(new Date());
		timeSeries.setRow(ts4, 10.0, 10.1, 10.2, 10.3);
		Thread.sleep(1);
		timeSeries.setRow(new TimeStamp(new Date()), 10.0, 10.1, 10.2, 10.3);

		// shift to right
		timeSeries.setRow(ts1, 1.0, 1.1, 1.2, 1.3);

		assertEquals(10, timeSeries.getNumRows());
		assertEquals(ts4, timeSeries.getMaxTimestamp());
		assertEquals(ts1, timeSeries.getMinTimestamp());
	}

	@Test
	public void testGetMaxWindow() {
		timeSeries.setMaxWindow(20);
		assertEquals(20, timeSeries.getMaxWindow());
	}

	@Test
	public void testDeleteBeforeTimeStamp() throws InterruptedException {
		timeSeries.delete(new TimeStamp(new Date(1336305521000L)),
				new TimeStamp(new Date(1336305521300L)));

		assertEquals(0, timeSeries.getNumRows());

		for (int i = 0; i < 5; i++) {
			timeSeries.setRow(new TimeStamp(new Date()), 1.0 + i, 1.1 + i,
					1.2 + i, 1.3 + i);
			Thread.sleep(1);
		}

		TimeStamp ts2 = new TimeStamp(new Date());
		timeSeries.setRow(ts2, 6.0, 6.1, 6.2, 6.3);
		Thread.sleep(1);

		for (int i = 0; i < 4; i++) {
			timeSeries.setRow(new TimeStamp(new Date()), 7.0 + i, 7.1 + i,
					7.2 + i, 7.3 + i);
			Thread.sleep(1);
		}

		timeSeries.deleteBefore(ts2);

		assertEquals(4, timeSeries.getNumRows());
	}

	@Test
	public void testDeleteBeforeIndex() throws InterruptedException {
		timeSeries.delete(new TimeStamp(new Date(1336305521000L)),
				new TimeStamp(new Date(1336305521300L)));

		assertEquals(0, timeSeries.getNumRows());

		for (int i = 0; i < 5; i++) {
			timeSeries.setRow(new TimeStamp(new Date()), 1.0 + i, 1.1 + i,
					1.2 + i, 1.3 + i);
			Thread.sleep(1);
		}

		TimeStamp ts2 = new TimeStamp(new Date());
		timeSeries.setRow(ts2, 6.0, 6.1, 6.2, 6.3);
		Thread.sleep(1);

		for (int i = 0; i < 4; i++) {
			timeSeries.setRow(new TimeStamp(new Date()), 7.0 + i, 7.1 + i,
					7.2 + i, 7.3 + i);
			Thread.sleep(1);
		}

		timeSeries.deleteBefore(4);

		assertEquals(5, timeSeries.getNumRows());
		assertEquals(ts2, timeSeries.getTime(0));

		timeSeries.deleteAfter(0);

		TimeStamp ts1 = new TimeStamp(new Date());
		timeSeries.setRow(ts1, 6.0, 6.1, 6.2, 6.3);
		Thread.sleep(1);

		timeSeries.deleteBefore(0);
		assertEquals(0, timeSeries.getNumRows());
	}

	@Test
	public void testDeleteAfterIndex() throws InterruptedException {
		timeSeries.delete(new TimeStamp(new Date(1336305521000L)),
				new TimeStamp(new Date(1336305521300L)));

		assertEquals(0, timeSeries.getNumRows());

		for (int i = 0; i < 5; i++) {
			timeSeries.setRow(new TimeStamp(new Date()), 1.0 + i, 1.1 + i,
					1.2 + i, 1.3 + i);
			Thread.sleep(1);
		}

		TimeStamp ts2 = new TimeStamp(new Date());
		timeSeries.setRow(ts2, 6.0, 6.1, 6.2, 6.3);
		Thread.sleep(1);

		for (int i = 0; i < 4; i++) {
			timeSeries.setRow(new TimeStamp(new Date()), 7.0 + i, 7.1 + i,
					7.2 + i, 7.3 + i);
			Thread.sleep(1);
		}

		timeSeries.deleteAfter(6);

		assertEquals(6, timeSeries.getNumRows());
		assertEquals(ts2, timeSeries.getTime(timeSeries.getIndex(timeSeries
				.getMaxTimestamp())));

		timeSeries.deleteAfter(0);

		TimeStamp ts1 = new TimeStamp(new Date());
		timeSeries.setRow(ts1, 6.0, 6.1, 6.2, 6.3);
		Thread.sleep(1);

		timeSeries.deleteAfter(0);
		assertEquals(0, timeSeries.getNumRows());
		// assertEquals(ts1,
		// timeSeries.getTime(timeSeries.getIndex(timeSeries.getMaxTimestamp())));
	}

	@Test
	public void testDeleteAfterTimeStamp() throws InterruptedException {
		timeSeries.delete(new TimeStamp(new Date(1336305521000L)),
				new TimeStamp(new Date(1336305521300L)));

		assertEquals(0, timeSeries.getNumRows());

		for (int i = 0; i < 5; i++) {
			timeSeries.setRow(new TimeStamp(new Date()), 1.0 + i, 1.1 + i,
					1.2 + i, 1.3 + i);
			Thread.sleep(1);
		}

		TimeStamp ts1 = new TimeStamp(new Date());
		timeSeries.setRow(ts1, 6.0, 6.1, 6.2, 6.3);
		Thread.sleep(1);

		TimeStamp ts2 = new TimeStamp(new Date());
		timeSeries.setRow(ts2, 7.0, 7.1, 7.2, 7.3);
		Thread.sleep(1);

		for (int i = 0; i < 3; i++) {
			timeSeries.setRow(new TimeStamp(new Date()), 7.0 + i, 7.1 + i,
					7.2 + i, 7.3 + i);
			Thread.sleep(1);
		}

		timeSeries.deleteAfter(ts2);

		assertEquals(6, timeSeries.getNumRows());
		assertEquals(ts1, timeSeries.getTime(timeSeries.getIndex(timeSeries
				.getMaxTimestamp())));

		timeSeries.deleteAfter(0);

		ts1 = new TimeStamp(new Date());
		timeSeries.setRow(ts1, 6.0, 6.1, 6.2, 6.3);
		Thread.sleep(1);

		timeSeries.deleteAfter(ts1);
		assertEquals(0, timeSeries.getNumRows());
		// assertEquals(ts1,
		// timeSeries.getTime(timeSeries.getIndex(timeSeries.getMaxTimestamp())));
	}

	@Test
	public void testAddTimeSeries() {
		List<TypedColumn> columns = new ArrayList<TypedColumn>();
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());
		columns.add(new DoubleColumn());

		TSContainer2 timeSeries2 = new TSContainer2(
				TSContainerTest.class.getName(), Arrays.asList(headers),
				columns);
		timeSeries2.setRow(new TimeStamp(new Date(1336305521100L)),
				new Double[] { 2.0, 2.1, 2.2, 2.3 });
		timeSeries2.setRow(new TimeStamp(new Date(1336305521250L)),
				new Double[] { 2.5, 2.51, 2.52, 2.53 });
		timeSeries2.setRow(new TimeStamp(new Date(1336305521000L)),
				new Double[] { 1.0, 1.1, 1.2, 1.3 });
		timeSeries2.setRow(new TimeStamp(new Date(1336305521200L)),
				new Double[] { 3.0, 3.1, 3.2, 3.3 });

		/*
		 * System.out.println("====");
		 * System.out.println(timeSeries.toString());
		 * System.out.println("====");
		 * System.out.println(timeSeries2.toString());
		 */
		timeSeries.addTimeSeries(timeSeries2, true);

		/*
		 * System.out.println("Sum"); System.out.println(timeSeries.toString());
		 */
		assertEquals(2.5,
				timeSeries.getRow(new TimeStamp(new Date(1336305521250L)))[0]);
		assertEquals(2.0,
				timeSeries.getRow(new TimeStamp(new Date(1336305521000L)))[0]);
		assertEquals(4.0,
				timeSeries.getRow(new TimeStamp(new Date(1336305521100L)))[0]);
	}

	@Test
	public void testGetTimeFrame() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
		TSContainer2 tsc = timeSeries.getTimeFrame(new TimeStamp(new Date(
				1336305521000L)), new TimeStamp(new Date(1336305521200L)));
		
		
/*		System.out.println("====");
		System.out.println(timeSeries.toString());
		System.out.println("====");
		System.out.println(tsc.toString());
*/		
		
		List<TypedColumn> columns = new ArrayList<TypedColumn>();
		columns.add(new StringColumn());
		columns.add(new StringColumn());
		columns.add(new StringColumn());
		columns.add(new StringColumn());

		TSContainer2 timeSeries2 = new TSContainer2(TSContainerTest.class.getName(),
				Arrays.asList(headers), columns);

		timeSeries2.setRow(new TimeStamp(new Date(1336305521100L)),
				new Double[] { 2.0, 2.1, 2.2, 2.3 });
		timeSeries2.setRow(new TimeStamp(new Date(1336305521300L)),
				new Double[] { 4.0, 4.1, 4.2, 4.3 });
		timeSeries2.setRow(new TimeStamp(new Date(1336305521000L)),
				new Double[] { 1.0, 1.1, 1.2, 1.3 });
		timeSeries2.setRow(new TimeStamp(new Date(1336305521200L)),
				new Double[] { 3.0, 3.1, 3.2, 3.3 });

		TSContainer2 tsc2 = timeSeries.getTimeFrame(new TimeStamp(new Date(
				1336305521000L)), new TimeStamp(new Date(1336305521200L)));

/*		System.out.println("====");
		System.out.println(timeSeries2.toString());
		System.out.println("====");
		System.out.println(tsc2.toString());
*/		
		assertEquals(2, tsc.getNumRows());
		assertEquals(2, tsc2.getNumRows());
		//fail("Not implemented");
	}
}
