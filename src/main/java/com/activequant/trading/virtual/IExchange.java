package com.activequant.trading.virtual;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.exceptions.IncompleteOrderInstructions;
import com.activequant.exceptions.NoSuchOrderBook;
import com.activequant.exceptions.UnsupportedOrderType;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.trading.IOrderTracker;

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

	public abstract LimitOrderBook getOrderBook(String tradeableInstrumentId) throws NoSuchOrderBook;

	public abstract void processStreamEvent(StreamEvent se);

}