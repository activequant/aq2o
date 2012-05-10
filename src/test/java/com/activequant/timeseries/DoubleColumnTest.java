package com.activequant.timeseries;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.activequant.domainmodel.TimeStamp;

public class DoubleColumnTest extends TestCase {
	DoubleColumn dc = new DoubleColumn();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dc.add(1.0);
		dc.add(2.0);
		dc.add(3.0);
		dc.add(4.0);
		dc.add((Double)null);
		dc.add(5.0);
	}

	@Test
	public void testCumsum() {
		DoubleColumn dc_ret = dc.cumsum();

		assertEquals(1.0, dc_ret.get(0));
		assertEquals(3.0, dc_ret.get(1));
		assertEquals(6.0, dc_ret.get(2));
		assertEquals(10.0, dc_ret.get(3));
		assertEquals(10.0, dc_ret.get(4));
		assertEquals(15.0, dc_ret.get(5));
	}

	@Test
	public void testMultiply() {
		DoubleColumn dc_ret = dc.multiply(10.0);

		assertEquals(10.0, dc_ret.get(0));
		assertEquals(20.0, dc_ret.get(1));
		assertEquals(30.0, dc_ret.get(2));
		assertEquals(40.0, dc_ret.get(3));
		assertEquals(0.0,  dc_ret.get(4));
		assertEquals(50.0, dc_ret.get(5));
	}

	@Test
	public void testAdd() {
		DoubleColumn other = new DoubleColumn();
		other.add(0.1);
		other.add(0.2);
		other.add(0.3);
		other.add((Double)null);
		other.add((Double)null);
		other.add(0.5);
		
		DoubleColumn dc_ret = dc.add(other);
		assertEquals(1.1, dc_ret.get(0));
		assertEquals(2.2, dc_ret.get(1));
		assertEquals(3.3, dc_ret.get(2));
		assertEquals(4.0, dc_ret.get(3));
		assertEquals(0.0, dc_ret.get(4));
		assertEquals(5.5, dc_ret.get(5));
	}
}
