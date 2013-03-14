package com.activequant.trading;

import java.util.HashMap;
import java.util.Map;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.IncompleteOrderInstructions;
import com.activequant.domainmodel.exceptions.NoSuchOrderBook;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.domainmodel.exceptions.UnsupportedOrderType;
import com.activequant.domainmodel.orderbook.MarketState;
import com.activequant.domainmodel.streaming.OrderStreamEvent;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.domainmodel.trade.order.SingleLegOrder;
import com.activequant.interfaces.trading.IExchange;
import com.activequant.interfaces.trading.IOrderTracker;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.trading.virtual.LimitOrderBook;
import com.activequant.utils.UniqueTimeStampGenerator;
import com.activequant.utils.events.Event;

/**
 * This transport exchange acts as an injectable exchange, based on a transport
 * factory. The Google protocol buffer approach. not that it's anything specific
 * which Google came up with, it's just that Google has a good open source
 * library for this stuff.
 * 
 * This exchange is a default transport exchange. It requires a
 * ITransportFactory to work properly. This transport exchange must be injected
 * into a trading system environment.
 * 
 * TODOs: this exchange should also fetch a list of open orders from somewhere (to be discussed) 
 * 
 * @author GhostRider
 * 
 */
public class DefaultTransportExchange implements IExchange {

	private final ITransportFactory transFac;
	private final Event<OrderStreamEvent> event = new Event<OrderStreamEvent>();
	private final Event<MarketState> marketStateEvent = new Event<MarketState>();

	private final Map<String, TransportOrderTracker> trackers = new HashMap<String, TransportOrderTracker>();
	private UniqueTimeStampGenerator utsg = new UniqueTimeStampGenerator();

	public DefaultTransportExchange(ITransportFactory transFac)
			throws TransportException {
		this.transFac = transFac;
	}

	@Override
	public TimeStamp currentExchangeTime() {
		return new TimeStamp();
	}

	@Override
	public IOrderTracker prepareOrder(Order order) throws UnsupportedOrderType,
			IncompleteOrderInstructions {
		if (order.getOrderId() == null)
			order.setOrderId("ID"+utsg.now().toString());

		// generate publisher and receiver for this order tracker ...
		//
		SingleLegOrder slo = (SingleLegOrder) order;
		String tdiId = slo.getTradInstId();

		//
		TransportOrderTracker tracker;
		try {
			tracker = new TransportOrderTracker(
					transFac.getPublisher(ETransportType.TRAD_DATA, tdiId),
					transFac.getReceiver(ETransportType.TRAD_DATA, tdiId), slo);
			trackers.put(order.getOrderId(), tracker);
			return tracker;
		} catch (TransportException e) {
			e.printStackTrace(); 
			// so dirty again, we would have to alter all the interface. 
			throw new UnsupportedOrderType("Transport Exception: " + e);
		}

	}

	@Override
	public IOrderTracker getOrderTracker(String orderId) {
		//
		return trackers.get(orderId);
	}

	@Override
	public AbstractOrderBook<?> getOrderBook(String tradeableInstrumentId)
			throws NoSuchOrderBook {
		LimitOrderBook lob = new LimitOrderBook(null, null);
		// ???
		return null;
	}

	@Override
	public void processStreamEvent(StreamEvent se) {
		// doing nothing.
	}

	public Event<OrderStreamEvent> getEvent() {
		return event;
	}

	public Event<MarketState> getMarketStateEvent() {
		return marketStateEvent;
	}

}
