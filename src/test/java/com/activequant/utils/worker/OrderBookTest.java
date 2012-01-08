package com.activequant.utils.worker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.trading.virtual.LimitOrderBook;

public class OrderBookTest extends TestCase {
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(OrderBookTest.class);
	}

	public void testAddingAndSorting1(){
		
		LimitOrder o1 = new LimitOrder();
		o1.setLimitPrice(100.0);
		o1.setOrderSide(OrderSide.BUY);
		
		LimitOrder o2 = new LimitOrder();
		o2.setLimitPrice(101.0);
		o2.setOrderSide(OrderSide.BUY);
		
		LimitOrderBook ob = new LimitOrderBook("TESTBOOK");
		ob.addOrder(o1);
		ob.addOrder(o2);
		
		assertEquals(o2, ob.getBuySide().get(0));
		assertEquals(o1, ob.getBuySide().get(1));		
		
	}
	

	public void testAddingAndSorting2(){
		
		LimitOrder o1 = new LimitOrder();
		o1.setLimitPrice(100.0);
		o1.setOrderSide(OrderSide.SELL);
		
		LimitOrder o2 = new LimitOrder();
		o2.setLimitPrice(101.0);
		o2.setOrderSide(OrderSide.SELL);
		
		LimitOrderBook ob = new LimitOrderBook("TESTBOOK");
		ob.addOrder(o1);
		ob.addOrder(o2);
		
		assertEquals(o1, ob.getSellSide().get(0));
		assertEquals(o2, ob.getSellSide().get(1));		
		
	}
}
