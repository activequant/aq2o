package com.activequant.trading;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.IncompleteOrderInstructions;
import com.activequant.domainmodel.exceptions.NoSuchOrderBook;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.domainmodel.exceptions.UnsupportedOrderType;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.trade.event.OrderAcceptedEvent;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.domainmodel.trade.order.SingleLegOrder;
import com.activequant.interfaces.trading.IExchange;
import com.activequant.interfaces.trading.IOrderTracker;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages;
import com.activequant.messages.Marshaller;
import com.google.protobuf.InvalidProtocolBufferException;

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
	Marshaller marshaller = new Marshaller();
	private Logger log = Logger.getLogger(DefaultTransportExchange.class);

	private Map<String, TransportOrderTracker> trackers = new HashMap<String, TransportOrderTracker>();
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
		if(log.isDebugEnabled())
			log.debug("Handling raw byte message. "); 
		// 
		AQMessages.BaseMessage bm;
		try {
			bm = marshaller.demarshall(rawMessage);
			switch (bm.getType()) {
			case ORD_ACCPTD:
				log.info("Order accepted.");
				AQMessages.OrderAccepted oa = ((AQMessages.OrderAccepted) bm
						.getExtension(AQMessages.OrderAccepted.cmd));
				handle(oa);
				break;
			
			}
		} catch (InvalidProtocolBufferException e) {
			log.warn("Could not demarshall message.");
		}
		
	}
	
	private void handle(AQMessages.OrderAccepted oa){
		TransportOrderTracker iot = trackers.get(oa.getClOrdId());
		if(iot!=null){
			// 
			OrderAcceptedEvent oae = new OrderAcceptedEvent(); 
			oae.setRefOrderId(oa.getClOrdId());
			iot.fireEvent(oae);			
		}
	}

	@Override
	public TimeStamp currentExchangeTime() {
		return new TimeStamp();
	}

	@Override
	public IOrderTracker prepareOrder(Order order) throws UnsupportedOrderType,
			IncompleteOrderInstructions {
		TransportOrderTracker tracker = new TransportOrderTracker(publisher, (SingleLegOrder) order);
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
