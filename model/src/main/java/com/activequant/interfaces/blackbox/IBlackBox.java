package com.activequant.interfaces.blackbox;

import com.activequant.messages.AQMessages;
import com.activequant.utils.events.Event;

/**
 * The black box interface holds all methods relevant for using a trading
 * service such as BrokerAQ.
 * 
 * The blackbox is a facade behind which various services are orchestrated.
 * 
 * This is, what was the AlgoEnvironment before.
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
	Event<AQMessages.MarketDataSnapshot> quoteEvent();

	/**
	 * all ticks arrive on this event.
	 * 
	 * @return
	 */
	Event<AQMessages.Tick> tickEvent();

	/**
	 * all OHLC data arrives on this event.
	 * 
	 * @return
	 */
	Event<AQMessages.OHLC> ohlcEvent();

	/**
	 * market data subscription responses arrive here.
	 * 
	 * @return
	 */
	Event<AQMessages.MDSubscribeResponse> subscriptionResponseEvent();

	/**
	 * All order related events, such as order submitted, order rejected or
	 * order done arrive here.
	 * 
	 * @return
	 */
	Event<AQMessages.BaseMessage> orderEvents();

	/**
	 * All acccount related messages arrive here.
	 * 
	 * @return
	 */
	Event<AQMessages.AccountDataMessage> accountDataEvent();

	/**
	 * Some servers send out server time, this arrives here.
	 * 
	 * @return
	 */
	Event<AQMessages.ServerTime> serverTimeEvent();

	/**
	 * Position/Portfolio messages arrive here.
	 * 
	 * @return
	 */
	Event<AQMessages.PositionReport> positionEvent();

	/**
	 * Order executions arrive here.
	 * 
	 * @return
	 */
	Event<AQMessages.ExecutionReport> executionEvent();
	
}
