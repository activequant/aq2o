package com.activequant.trading.virtual;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.exceptions.IncompleteOrderInstructions;
import com.activequant.exceptions.NoSuchOrderBook;
import com.activequant.exceptions.UnsupportedOrderType;
import com.activequant.trading.IOrderTracker;

public interface IExchange {

	public abstract TimeStamp currentExchangeTime();

	public abstract IOrderTracker prepareOrder(Order order) throws UnsupportedOrderType, IncompleteOrderInstructions;

	public abstract LimitOrderBook getOrderBook(String tradeableInstrumentId) throws NoSuchOrderBook;

}