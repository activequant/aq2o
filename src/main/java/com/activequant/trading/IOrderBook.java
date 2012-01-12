package com.activequant.trading;

import com.activequant.trading.orderbook.IOrderBookListener;
import com.activequant.trading.orderbook.MarketState;

public interface IOrderBook {

	 
	String getTradeableInstrumentId();
	MarketState getMarketState(); 
	
	void attachOrderBookListener(IOrderBookListener listener);
	void detachOrderBookListener(IOrderBookListener listener);
	
	
}
