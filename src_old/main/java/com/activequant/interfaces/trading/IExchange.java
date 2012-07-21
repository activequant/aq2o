package com.activequant.interfaces.trading;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.IncompleteOrderInstructions;
import com.activequant.domainmodel.exceptions.NoSuchOrderBook;
import com.activequant.domainmodel.exceptions.UnsupportedOrderType;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.trading.AbstractOrderBook;

public interface IExchange {

	public abstract TimeStamp currentExchangeTime();

	public abstract IOrderTracker prepareOrder(Order order) throws UnsupportedOrderType, IncompleteOrderInstructions;
	
	/**
	 * May, but does not have to be implemented. 
	 * 
	 * @param orderId
	 * @return
	 */
	public abstract IOrderTracker getOrderTracker(String orderId);

	public abstract AbstractOrderBook<?> getOrderBook(String tradeableInstrumentId) throws NoSuchOrderBook;

	public abstract void processStreamEvent(StreamEvent se);

}