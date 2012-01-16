package com.activequant.trading.orderbook;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.domainmodel.Tuple;
import com.activequant.tools.streaming.BBOEvent;
import com.activequant.trading.virtual.LimitOrderBook;
import com.activequant.trading.virtual.VirtualExchange;
import com.activequant.utils.Date8Time6Parser;
import com.activequant.utils.UniqueTimeStampGenerator;

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
		UniqueTimeStampGenerator gen = new UniqueTimeStampGenerator(); 
		//  
		BBOEvent n = new BBOEvent(tradInstId, tradInstId, gen.now(),99.0,100.0,101.0,100.0);
		
		ve.processStreamEvent(n);
				
		assertNotNull(orderBookChange);
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
