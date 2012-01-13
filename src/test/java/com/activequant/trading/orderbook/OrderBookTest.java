package com.activequant.trading.orderbook;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.trading.virtual.LimitOrderBook;
import com.activequant.trading.virtual.VirtualExchange;

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
		
		LimitOrderBook ob = new LimitOrderBook(new VirtualExchange(), "TESTBOOK");
		ob.addOrder(o1);
		ob.addOrder(o2);
		
		assertEquals(o2, ob.buySide().get(0));
		assertEquals(o1, ob.buySide().get(1));		
		
	}
	

	public void testAddingAndSorting2(){
		
		LimitOrder o1 = new LimitOrder();
		o1.setLimitPrice(100.0);
		o1.setOrderSide(OrderSide.SELL);
		
		LimitOrder o2 = new LimitOrder();
		o2.setLimitPrice(101.0);
		o2.setOrderSide(OrderSide.SELL);
		
		LimitOrderBook ob = new LimitOrderBook(new VirtualExchange(), "TESTBOOK");
		ob.addOrder(o1);
		ob.addOrder(o2);
		
		assertEquals(o1, ob.sellSide().get(0));
		assertEquals(o2, ob.sellSide().get(1));		
		
	}
}
