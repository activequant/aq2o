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
import com.activequant.domainmodel.streaming.OrderStreamEvent;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.trade.event.OrderAcceptedEvent;
import com.activequant.domainmodel.trade.event.OrderCancellationRejectedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelledEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.domainmodel.trade.event.OrderRejectedEvent;
import com.activequant.domainmodel.trade.event.OrderReplacedEvent;
import com.activequant.domainmodel.trade.event.OrderSubmittedEvent;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.domainmodel.trade.order.SingleLegOrder;
import com.activequant.interfaces.trading.IExchange;
import com.activequant.interfaces.trading.IOrderTracker;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages;
import com.activequant.messages.AQMessages.OrderRejected;
import com.activequant.messages.Marshaller;
import com.activequant.utils.UniqueTimeStampGenerator;
import com.activequant.utils.events.Event;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * This transport exchange acts as an injectable exchange, based on a transport
 * factory. The Google protocol buffer approach. not that it's anything specific
 * which Google came up with, it's just that Google has a good open source
 * library out.
 * 
 * @author GhostRider
 * 
 */
public class DefaultTransportExchange implements IExchange {

	private final ITransportFactory transFac;
	private final IPublisher publisher;
	private final IReceiver receiver;
	private final Marshaller marshaller = new Marshaller();
	private final Logger log = Logger.getLogger(DefaultTransportExchange.class);
	private final Event<OrderStreamEvent> event = new Event<OrderStreamEvent>();
	private final Map<String, TransportOrderTracker> trackers = new HashMap<String, TransportOrderTracker>();
	private UniqueTimeStampGenerator utsg = new UniqueTimeStampGenerator();

	public DefaultTransportExchange(ITransportFactory transFac)
			throws TransportException {
		this.transFac = transFac;
		this.publisher = transFac.getPublisher(ETransportType.TRAD_DATA,
				"OUTBOUND");
		// have to subscribe to the control channel or to some other channel
		// over which we are going to receive information
		// from the transport counter point.
		this.receiver = transFac.getReceiver(ETransportType.TRAD_DATA,
				"INBOUND");
		receiver.getRawEvent().addEventListener(new IEventListener<byte[]>() {
			@Override
			public void eventFired(byte[] arg0) {
				handle(arg0);
			}
		});
	}

	/**
	 * Handles incoming raw byte messages. Rohes Fleisch in ihrer reinsten Form.
	 * 
	 * @param rawMessage
	 */
	private void handle(byte[] rawMessage) {
		if (log.isDebugEnabled())
			log.debug("Handling raw byte message. ");
		//
		AQMessages.BaseMessage bm;
		try {
			bm = marshaller.demarshall(rawMessage);
			switch (bm.getType()) {
			case ORD_SUBMITTED:
				log.info("Order submitted.");
				AQMessages.OrderSubmitted os = ((AQMessages.OrderSubmitted) bm
						.getExtension(AQMessages.OrderSubmitted.cmd));
				handle(os);
				break;

			case ORD_ACCPTD:
				log.info("Order accepted.");
				AQMessages.OrderAccepted oa = ((AQMessages.OrderAccepted) bm
						.getExtension(AQMessages.OrderAccepted.cmd));
				handle(oa);
				break;
			case ORD_CANCELLED:
				log.info("Order cancelled.");
				AQMessages.OrderCancelled oc = ((AQMessages.OrderCancelled) bm
						.getExtension(AQMessages.OrderCancelled.cmd));
				handle(oc);
				break;
			case ORD_CNCL_REJ:
				log.info("Order cancellation rejected.");
				AQMessages.OrderCancelReject ocr = ((AQMessages.OrderCancelReject) bm
						.getExtension(AQMessages.OrderCancelReject.cmd));
				handle(ocr);
				break;
			case ORD_UPDATED:
				log.info("Order updated.");
				AQMessages.OrderUpdated ou = ((AQMessages.OrderUpdated) bm
						.getExtension(AQMessages.OrderUpdated.cmd));
				handle(ou);
				break;

			case ORD_REJ:
				log.info("Order rejected.");
				AQMessages.OrderRejected or = ((AQMessages.OrderRejected) bm
						.getExtension(AQMessages.OrderRejected.cmd));
				handle(or);
				break;
			case EXECUTION_REPORT:
				AQMessages.ExecutionReport er = ((AQMessages.ExecutionReport) bm
						.getExtension(AQMessages.ExecutionReport.cmd));
				handle(er);
				break;
			}

		} catch (InvalidProtocolBufferException e) {
			log.warn("Could not demarshall message.");
		}

	}

	private void handle(AQMessages.OrderSubmitted oa) {
		// should do something about UPD messages.
		String ordId = oa.getClOrdId();
		if (ordId.startsWith("UPDT")) {
			ordId = ordId.split(":")[1];
		}
		TransportOrderTracker iot = trackers.get(ordId);
		if (iot != null) {
			//
			OrderSubmittedEvent oae = new OrderSubmittedEvent();
			oae.setRefOrderId(ordId);
			oae.setRefOrder(iot.getOrder());

			iot.fireEvent(oae);
			OrderStreamEvent ose = new OrderStreamEvent("", utsg.now(), oae);
			this.event.fire(ose);
		}
	}

	private void handle(OrderRejected or) {
		String orderId = or.getClOrdId();
		if (orderId.startsWith("UPDT:")) {
			// alright, let's split it.
			orderId = orderId.split(":")[1];
		}
		TransportOrderTracker iot = trackers.get(orderId);
		if (iot != null) {
			//
			OrderRejectedEvent oae = new OrderRejectedEvent();
			oae.setRefOrderId(orderId);
			oae.setRefOrder(iot.getOrder());
			oae.setReason(or.getReason());
			iot.fireEvent(oae);
			OrderStreamEvent ose = new OrderStreamEvent("", utsg.now(), oae);
			this.event.fire(ose);
		}
	}

	private void handle(AQMessages.OrderAccepted oa) {
		TransportOrderTracker iot = trackers.get(oa.getClOrdId());
		if (iot != null) {
			//
			OrderAcceptedEvent oae = new OrderAcceptedEvent();
			oae.setRefOrderId(oa.getClOrdId());
			oae.setRefOrder(iot.getOrder());
			iot.fireEvent(oae);
			OrderStreamEvent ose = new OrderStreamEvent("", utsg.now(), oae);
			this.event.fire(ose);
		}
	}

	private void handle(AQMessages.ExecutionReport er) {

		String orderId = er.getClOrdId();
		if (orderId.startsWith("UPDT:")) {
			// alright, let's split it.
			orderId = orderId.split(":")[1];
		}
		TransportOrderTracker iot = trackers.get(orderId);
		if (iot != null) {

			OrderFillEvent ofe = new OrderFillEvent();
			double leftQuantity = er.getLeavesQty();
			ofe.setLeftQuantity(leftQuantity);

			Double cumQty = er.getCumQty();
			Double avgPx = er.getAvgPx();

			String side = er.getSide() == '2' ? "S" : "B";
			ofe.setSide(side);

			ofe.setFillPrice(er.getPrice());
			ofe.setFillAmount(cumQty);
			ofe.setLeftQuantity(er.getLeavesQty());
			ofe.setOptionalInstId(er.getTradInstId());
			ofe.setRefOrderId(orderId);
			ofe.setRefOrder(iot.getOrder());
			iot.fireEvent(ofe);
			OrderStreamEvent ose = new OrderStreamEvent("", utsg.now(), ofe);
			this.event.fire(ose);
		}
	}

	private void handle(AQMessages.OrderCancelled oc) {
		// have to extract the central order id.
		String orderId = oc.getClOrdId();
		if (orderId.startsWith("CNCL:")) {
			// alright, let's split it.
			orderId = orderId.split(":")[1];
		}

		//
		TransportOrderTracker iot = trackers.get(orderId);
		if (iot != null) {
			//
			OrderCancelledEvent oce = new OrderCancelledEvent();
			oce.setRefOrderId(orderId);
			oce.setRefOrder(iot.getOrder());
			iot.fireEvent(oce);
			OrderStreamEvent ose = new OrderStreamEvent("", utsg.now(), oce);
			this.event.fire(ose);
		}
	}

	private void handle(AQMessages.OrderUpdated ou) {
		String orderId = ou.getClOrdId();
		if (orderId.startsWith("UPDT:")) {
			// alright, let's split it.
			orderId = orderId.split(":")[1];
		}
		TransportOrderTracker iot = trackers.get(orderId);
		if (iot != null) {
			//
			iot.signalSuccessfullUpdate();
			OrderReplacedEvent ore = new OrderReplacedEvent();
			ore.setRefOrderId(orderId);
			ore.setRefOrder(iot.getOrder());
			OrderStreamEvent ose = new OrderStreamEvent("", utsg.now(), ore);
			this.event.fire(ose);

		}

	}

	private void handle(AQMessages.OrderCancelReject ocr) {
		String orderId = ocr.getClOrdId();
		if (orderId.startsWith("CNCL:")) {
			// alright, let's split it.
			orderId = orderId.split(":")[1];
		}
		TransportOrderTracker iot = trackers.get(orderId);
		if (iot != null) {
			//
			OrderCancellationRejectedEvent oce = new OrderCancellationRejectedEvent();
			oce.setReason(ocr.getClxRejReason());
			oce.setRefOrderId(orderId);
			oce.setRefOrder(iot.getOrder());
			iot.fireEvent(oce);
			OrderStreamEvent ose = new OrderStreamEvent("", utsg.now(), oce);
			this.event.fire(ose);
		}
	}

	@Override
	public TimeStamp currentExchangeTime() {
		return new TimeStamp();
	}

	@Override
	public IOrderTracker prepareOrder(Order order) throws UnsupportedOrderType,
			IncompleteOrderInstructions {
		if (order.getOrderId() == null)
			order.setOrderId(utsg.now().toString());
		TransportOrderTracker tracker = new TransportOrderTracker(publisher,
				(SingleLegOrder) order);
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

	public Event<OrderStreamEvent> getEvent() {
		return event;
	}

}
