package com.activequant.trading.orderbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.trading.virtual.LimitOrderBook;
import com.activequant.trading.virtual.VirtualExchange;
import com.activequant.transport.memory.InMemoryTransportFactory;

public class LimitOrderBookTests extends TestCase {
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(LimitOrderBookTests.class);
	}
	private int events = 0;
	public void testInitFromStringStringMap(){
		//
		events = 0; 
		IOrderBookListener iobl = new IOrderBookListener() {			
			@Override
			public void transactionEvent(TransactionEvent te) { 
				events++; 
			}
			@Override
			public void orderBookChange(OrderBookChange obc) {
				// 
			}	
			@Override
			public void marketStateChange(MarketState newState) {
				events++;
			}
		};
		
		// 
		class LocalLimitOrderBook extends LimitOrderBook{
			public LocalLimitOrderBook(String tradeableInstrumentId) {super(new VirtualExchange(new InMemoryTransportFactory()), tradeableInstrumentId);}
			public void fireTransaction(TransactionEvent te){super.transaction(te);};
			public void fireMarketStateChange(MarketState newState){super.marketStateChange(newState);};			
		}
		//
		LocalLimitOrderBook llob = new LocalLimitOrderBook("ID"); 
		llob.attachOrderBookListener(iobl);	
		TransactionEvent te1 = new ExecutionEvent();
		TransactionEvent te2 = new ExecutionCanceledEvent();
		
		llob.fireTransaction(te1);
		llob.fireTransaction(te2);
		
		assertEquals(2, events);
		
		llob.detachOrderBookListener(iobl);
		llob.fireTransaction(te2);
		assertEquals(2, events);
		events = 0; 
		
		llob.attachOrderBookListener(iobl);	
		llob.fireMarketStateChange(new MarketOpen());
		assertEquals("OPEN", llob.getMarketState().getType());
		assertEquals(1, events);
		llob.fireMarketStateChange(new MarketClosed());	
		assertEquals("CLOSED", llob.getMarketState().getType());
		assertEquals(2, events);
	}
	
	public void testDataRelaying(){
		List<LimitOrder> list = new ArrayList<LimitOrder>();
		LimitOrder l1 = new LimitOrder();
		l1.setLimitPrice(100.0);
		LimitOrder l2 = new LimitOrder();
		l2.setLimitPrice(200.0);
		list.add(l1);
		list.add(l2);
		Collections.sort(list, new Comparator<LimitOrder>() {
			@Override
			public int compare(LimitOrder o1, LimitOrder o2) {
				return (int) (o1.getLimitPrice() - o2.getLimitPrice());
			}
		});	
		for (LimitOrder l : list) {
			System.out.println(l.getLimitPrice());
		}
	}
	
}
