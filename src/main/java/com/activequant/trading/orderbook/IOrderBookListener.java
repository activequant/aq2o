package com.activequant.trading.orderbook;


public interface IOrderBookListener  {
	
	void orderBookChange(OrderBookChange obc);
	void transactionEvent(TransactionEvent te); 
	void marketStateChange(MarketState newState);
	
}
