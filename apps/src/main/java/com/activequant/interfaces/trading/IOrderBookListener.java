package com.activequant.interfaces.trading;

import com.activequant.domainmodel.orderbook.MarketState;
import com.activequant.domainmodel.orderbook.OrderBookChange;
import com.activequant.domainmodel.orderbook.TransactionEvent;


public interface IOrderBookListener  {
	
	void orderBookChange(OrderBookChange obc);
	void transactionEvent(TransactionEvent te); 
	void marketStateChange(MarketState newState);
	
}
