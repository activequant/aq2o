package com.activequant.interfaces.trading;

import java.util.List;

import com.activequant.domainmodel.orderbook.MarketState;
import com.activequant.domainmodel.trade.order.Order;

public interface IOrderBook<T extends Order> {

	
	
	String getTradeableInstrumentId();
	
	/**
	 * returns the current market state, for example open, preopen, etc. 
	 * 
	 * @return
	 */
	MarketState getMarketState(); 
	
	/**
	 * Add an order book listener. 
	 * 
	 * Note: thought about using the event pattern ... decided against it for now. might change in the future.  
	 * 
	 * @param listener
	 */
	void attachOrderBookListener(IOrderBookListener listener);
	
	/**
	 * Remove an order book listener 
	 * 
	 * @param listener
	 */
	void detachOrderBookListener(IOrderBookListener listener);
	
	/**
	 * Although the orders SHOULD be sorted, there is no guarantee. See specific orderbook implementation details
	 * 
	 * @return the entire buy side of orders. 
	 */
	List<T> buySide();
	
	/**
	 * Although the orders SHOULD be sorted, there is no guarantee. See specific orderbook implementation details
	 * 
	 * @return the entire sell side of orders. 
	 */
	List<T> sellSide(); 
	
	
}
