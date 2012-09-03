package com.activequant.trading;

import java.util.HashMap;
import java.util.Map;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.IncompleteOrderInstructions;
import com.activequant.domainmodel.exceptions.NoSuchOrderBook;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.domainmodel.exceptions.UnsupportedOrderType;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.domainmodel.trade.order.SingleLegOrder;
import com.activequant.interfaces.trading.IExchange;
import com.activequant.interfaces.trading.IOrderTracker;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;

/**
 * This transport exchange acts as an injectable exchange. Supports only single leg order. 
 * 
 * @author GhostRider
 *
 */
public class DefaultTransportExchange implements IExchange {
	
	private final ITransportFactory transFac; 
	private final IPublisher publisher; 
	private final IReceiver receiver; 
	private Map<String, IOrderTracker> trackers = new HashMap<String, IOrderTracker>();
	public DefaultTransportExchange(ITransportFactory transFac) throws TransportException{
		this.transFac = transFac; 
		this.publisher = transFac.getPublisher(ETransportType.TRAD_DATA, "OUTBOUND");
		// have to subscribe to the control chanel or to some other channel over which we are going to receive information 
		// from the transport counter point. 
		this.receiver = transFac.getReceiver(ETransportType.TRAD_DATA, "INBOUND");
		receiver.getRawEvent().addEventListener(new IEventListener<byte[]>() {
			@Override
			public void eventFired(byte[] arg0) {
				handle(arg0);
			}
		});
	}
	
	/**
	 * Handles incoming raw byte messages. 
	 * Rohes Fleisch in ihrer reinsten Form. 
	 * 
	 * @param rawMessage
	 */
	private void handle(byte[] rawMessage){
		// 
		
	}

	@Override
	public TimeStamp currentExchangeTime() {
		return new TimeStamp();
	}

	@Override
	public IOrderTracker prepareOrder(Order order) throws UnsupportedOrderType,
			IncompleteOrderInstructions {
		IOrderTracker tracker = new TransportOrderTracker(publisher, (SingleLegOrder) order);
		trackers.put(order.getOrderId(), tracker);	
		return tracker; 
	}

	@Override
	public IOrderTracker getOrderTracker(String orderId) {
		// 
		return trackers.get(orderId);
	}

	@Override
	public AbstractOrderBook<?> getOrderBook(String tradeableInstrumentId)
			throws NoSuchOrderBook {
		return null;
	}

	@Override
	public void processStreamEvent(StreamEvent se) {		
		// doing nothing. 
	}

}
