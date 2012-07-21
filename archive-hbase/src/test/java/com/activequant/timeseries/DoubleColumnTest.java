package com.activequant.timeseries;

import junit.framework.TestCase;
import org.junit.Test;

public class DoubleColumnTest extends TestCase {
	DoubleColumn dc = new DoubleColumn();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		dc.add(1.0);
		dc.add(2.0);
		dc.add(3.0);
		dc.add(4.0);
		dc.add((Double) null);
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
		assertEquals(0.0, dc_ret.get(4));
		assertEquals(50.0, dc_ret.get(5));
	}

	@Test
	public void testAdd() {
		DoubleColumn other = new DoubleColumn();
		other.add(0.1);
		other.add(0.2);
		other.add(0.3);
		other.add((Double) null);
		other.add((Double) null);
		other.add(0.5);

		DoubleColumn dc_ret = dc.add(other);
		assertEquals(1.1, dc_ret.get(0));
		assertEquals(2.2, dc_ret.get(1));
		assertEquals(3.3, dc_ret.get(2));
		assertEquals(4.0, dc_ret.get(3));
		assertEquals(0.0, dc_ret.get(4));
		assertEquals(5.5, dc_ret.get(5));
	}

	@Test
	public void testReturns() {
		DoubleColumn other = new DoubleColumn();
		other.add(1.0);
		other.add(2.0);
		other.add(3.0);
		other.add(4.0);
		other.add(5.0);
		other.add(6.0);
		other.add(7.0);
		other.add(8.0);
		other.add(9.0);
		other.add(10.0);

		DoubleColumn dc_ret = other.returns();
		assertEquals(null, dc_ret.get(0));
		assertEquals((2.0 - 1.0) / 1.0, dc_ret.get(1));
		assertEquals((3.0 - 2.0) / 2.0, dc_ret.get(2));
		assertEquals((5.0 - 4.0) / 4.0, dc_ret.get(4));
		assertEquals((10.0 - 9.0) / 9.0, dc_ret.get(9));
	}

	@Test
	public void testMean() {
		DoubleColumn other = new DoubleColumn();
		other.add(1.0);
		other.add(2.0);
		other.add(3.0);
		other.add(4.0);

		assertEquals(
				((2.0 - 1.0) / 1.0 + (3.0 - 2.0) / 2.0 + (4.0 - 3.0) / 3.0) / 3,
				other.mean());
	}

	@Test
	public void testStd() {
		DoubleColumn other = new DoubleColumn();
		other.add(1.0);
		other.add(2.0);
		other.add(3.0);
		other.add(4.0);

		double dev = other.std();
		assertEquals(0.283278862, dev, 0.000000001);
	}

	@Test
	public void testMaxDrawdown() {
		DoubleColumn other = new DoubleColumn();
		other.add(1.0);
		other.add(2.0);
		other.add(3.0);
		other.add(4.0);
		other.add(5.0);
		other.add(6.0);

		double drawdown = other.maxDrawdown();
		assertEquals(0.0, drawdown, 0.000000001);

		other.clear();
		other.add(4.0);
		other.add(3.0);
		other.add(2.0);
		other.add(1.0);
		drawdown = other.maxDrawdown();
		assertEquals(0.75, drawdown, 0.000000001);

		// this test data is taken from matlab
		// http://www.mathworks.com/help/toolbox/finance/bqxcira-1.html#bqxcira-3
		other.clear();
		other.add(1.00000);
		other.add(1.01680);
		other.add(1.00600);
		other.add(1.00940);
		other.add(0.99060);
		other.add(0.95165);
		other.add(0.95500);
		other.add(0.98522);
		other.add(0.99530);
		other.add(0.99127);
		other.add(0.99060);
		other.add(1.01280);
		other.add(0.99396);
		other.add(0.99328);
		other.add(0.95097);
		other.add(0.89993);
		other.add(0.90934);
		other.add(0.84822);
		other.add(0.88784);
		other.add(0.92881);
		other.add(0.90463);
		other.add(0.89053);
		other.add(0.88046);
		other.add(0.87777);
		other.add(0.92478);
		other.add(0.97045);
		other.add(0.97985);
		other.add(0.97246);
		other.add(0.98187);
		other.add(0.98522);
		other.add(1.01280);
		other.add(1.02150);
		other.add(1.06180);
		other.add(1.07450);
		other.add(1.09200);
		other.add(1.08870);
		other.add(1.06380);
		other.add(1.06510);
		other.add(1.08260);
		other.add(1.06720);
		other.add(1.07920);
		other.add(1.08530);
		other.add(1.09740);
		other.add(1.12290);
		other.add(1.15040);
		other.add(1.14040);
		other.add(1.15780);
		other.add(1.14370);
		other.add(1.13230);
		other.add(1.15580);
		other.add(1.16520);
		other.add(1.18330);
		other.add(1.18400);
		other.add(1.18600);
		other.add(1.16320);
		other.add(1.19070);
		other.add(1.19680);
		other.add(1.21630);
		other.add(1.22230);
		other.add(1.22430);
		other.add(1.23440);
		drawdown = other.maxDrawdown();
		assertEquals(0.1658, drawdown, 0.00001);
	}

	// I am not sure if the result of this method is correct
	// test is based on my understanding
	@Test
	public void testMaxRecovery() {
		DoubleColumn other = new DoubleColumn();
		other.add(10.0);
		other.add(9.0);
		other.add(8.0);
		other.add(7.0);
		other.add(6.0);
		other.add(5.0);
		other.add(4.0);
		other.add(3.0);
		other.add(2.0);
		other.add(1.0);

		assertEquals((Integer)9, other.maxRecoveryTime());
	}
}
