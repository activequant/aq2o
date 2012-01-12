package com.activequant.trading.orderbook;

import com.activequant.utils.events.IEventListener;
import com.activequant.utils.events.IEventSource;

public interface IOrderBookListener {

	/**
	 * 
	 */
	IEventSource<OrderBookChange> orderBookChangeEvent = null;
	
	
	IEventSource<TransactionEvent> transactionEvent = null;
	/**
	 * market phase = market state
	 * 
	 * see 
	 * http://www.asx.com.au/products/ASX-Trading%20hours-Market-phases.htm
	 * http://w3.nbdb.ca/customer_care/order_handling.jsp?menu=6_0
	 * 
	 */
	IEventSource<MarketState> marketStateChangeEvent = null;
	
}
