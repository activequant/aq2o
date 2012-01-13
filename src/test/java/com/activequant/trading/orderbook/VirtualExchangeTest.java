package com.activequant.trading.orderbook;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.Tuple;
import com.activequant.trading.NBBOEvent;
import com.activequant.trading.virtual.LimitOrderBook;
import com.activequant.trading.virtual.VirtualExchange;

public class VirtualExchangeTest extends TestCase {
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(VirtualExchangeTest.class);
	}

	OrderBookChange orderBookChange = null; 
	TransactionEvent transactionEvent = null; 
	MarketState marketState = null; 
	public void testAddingAndSorting1(){
	
		//
		orderBookChange = null;
		transactionEvent = null; 
		marketState = null; 
		//
		
		VirtualExchange ve = new VirtualExchange();
		
		String tradInstId = "TESTINST";
		
		LimitOrderBook lob = ve.getOrderBook(tradInstId);
		IOrderBookListener iobl = new IOrderBookListener() {			
			@Override
			public void transactionEvent(TransactionEvent te) { 
				transactionEvent = te; 
			}
			@Override
			public void orderBookChange(OrderBookChange obc) {
				orderBookChange = obc; 
			}	
			@Override
			public void marketStateChange(MarketState newState) {
				marketState = newState; 
			}
		};
		
		lob.attachOrderBookListener(iobl);
		
		//  
		NBBOEvent n = new NBBOEvent(tradInstId, Date8Time6.now(), 
				new Tuple<Double, Double>(99.0,100.0), new Tuple<Double, Double>(101.0,100.0));
		
		ve.processStreamEvent(n);
				
		assertNull(orderBookChange);
		assertEquals(ChangeTypeEnum.UPDATED, orderBookChange.getChangeType());
		
		// 
		assertEquals(1,lob.buySide().size());
		assertEquals(1,lob.sellSide().size());
		
		// 
		assertEquals(99.0, lob.buySide().get(0).getLimitPrice());
		assertEquals(100.0, lob.buySide().get(0).getQuantity());
		assertEquals(101.0, lob.sellSide().get(0).getLimitPrice());
		assertEquals(100.0, lob.sellSide().get(0).getQuantity());
		
		// run the matcher. 
		
		
	}
}
