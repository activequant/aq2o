package com.activequant.interfaces.blackbox;

import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.interfaces.utils.IEventSource;
import com.activequant.messages.AQMessages;
import com.activequant.messages.AQMessages.BaseMessage;

/**
 * The black box interface holds all methods relevant for using a trading
 * service such as BrokerAQ.
 * 
 * The blackbox is a facade behind which various services are orchestrated.
 * 
 * This is, what was the AlgoEnvironment before.
 * 
 * Currently not implemented: market state events.
 * 
 * @author GhostRider
 * 
 */
public interface IBlackBox {

	/**
	 * all quotes arrive on this event.
	 * 
	 * @return
	 */
	IEventSource<AQMessages.MarketDataSnapshot> quoteEvent();

	/**
	 * all ticks arrive on this event.
	 * 
	 * @return
	 */
	IEventSource<AQMessages.Tick> tickEvent();

	/**
	 * all OHLC data arrives on this event.
	 * 
	 * @return
	 */
	IEventSource<AQMessages.OHLC> ohlcEvent();

	/**
	 * market data subscription responses arrive here.
	 * 
	 * @return
	 */
	IEventSource<AQMessages.MDSubscribeResponse> subscriptionResponseEvent();

	/**
	 * All order related events, such as order submitted, order rejected or
	 * order done arrive here.
	 * 
	 * @return
	 */
	IEventSource<OrderEvent> orderEvents();

	/**
	 * All acccount related messages arrive here.
	 * 
	 * @return
	 */
	IEventSource<AQMessages.AccountDataMessage> accountDataEvent();

	/**
	 * Some servers send out server time, this arrives here.
	 * 
	 * @return
	 */
	IEventSource<AQMessages.ServerTime> serverTimeEvent();

	/**
	 * Position/Portfolio messages arrive here.
	 * 
	 * @return
	 */
	IEventSource<AQMessages.PositionReport> positionEvent();

	
	/**
	 * This method is called once the black box has connected the socket.
	 * 
	 * @return
	 */
	IEventSource<String> connected();

	/**
	 * Called when the black box has been disconnected
	 * 
	 * @return
	 */
	IEventSource<String> disconnected();

	/**
	 * Called when the black box is ready, for example once the black box
	 * container has successfully logged in.
	 * 
	 * @return
	 */
	IEventSource<String> ready();

	/**
	 * All messages that are being sent to the server have to pushed through
	 * this method.
	 * 
	 * @param bm
	 */
	void send(BaseMessage bm);	

}
