package com.activequant.trading;

import java.util.Date;

import javax.jms.Session;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.domainmodel.streaming.OrderStreamEvent;
import com.activequant.domainmodel.trade.event.OrderCancelSubmittedEvent;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderReplacedEvent;
import com.activequant.domainmodel.trade.event.OrderUpdateSubmittedEvent;
import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.MarketOrder;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.domainmodel.trade.order.SingleLegOrder;
import com.activequant.domainmodel.trade.order.StopOrder;
import com.activequant.interfaces.trading.IOrderTracker;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.interfaces.utils.IEventSource;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.MessageFactory;
import com.activequant.utils.UniqueTimeStampGenerator;
import com.activequant.utils.events.Event;
import com.google.protobuf.Message;

public class TransportOrderTracker implements IOrderTracker {

	private SingleLegOrder orderContainer;
	private SingleLegOrder pendingOrderContainer;
	private Event<OrderEvent> event = new Event<OrderEvent>();
	UniqueTimeStampGenerator utsg = new UniqueTimeStampGenerator();
	private Logger log = Logger.getLogger(TransportOrderTracker.class);
	private int seqCounter = 0;
	private OrderEvent lastState;

	private MessageFactory messageFactory;
	private IPublisher transportPublisher; 

	public TransportOrderTracker(IPublisher publisher, SingleLegOrder order) {
		this.transportPublisher = publisher; 		  
		this.orderContainer = order;
		event.addEventListener(new IEventListener<OrderEvent>() {
			@Override
			public void eventFired(OrderEvent event) {
				lastState = event;
			}
		});
		messageFactory = new MessageFactory();
	}

	@Override
	public Order getOrder() {
		return orderContainer;
	}

	public void fireEvent(OrderEvent oe) {
		event.fire(oe);
	}

	@Override
	public String getVenueAssignedId() {
		// not using a venue assigned ID, as TT is reusing our IDs.
		return orderContainer.getOrderId();
	}

	@Override
	public void submit() {
		Order o = orderContainer;
		String tradInstId = orderContainer.getTradInstId();
		BaseMessage bm = null;
		if (o instanceof LimitOrder) {
			LimitOrder lo = (LimitOrder) o;
			bm = messageFactory.orderLimitOrder(utsg.generate(new Date())
					.toString(), tradInstId, lo.getQuantity(), lo
					.getLimitPrice(), lo.getOrderSide());
		} else if (o instanceof MarketOrder) {
			MarketOrder mo = (MarketOrder) o;
			bm = messageFactory.orderMktOrder(utsg.generate(new Date())
					.toString(), tradInstId, mo.getQuantity(), mo
					.getOrderSide());
		} else if (o instanceof StopOrder) {
			StopOrder so = (StopOrder) o;
			bm = messageFactory.orderStopOrder(utsg.generate(new Date())
					.toString(), tradInstId, so.getQuantity(), so
					.getStopPrice(), so.getOrderSide());
		}
		if (bm != null) {
			try {
				transportPublisher.send(bm.toByteArray());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	/**
	 * Functionality is limited to updating limit order price and limit order size. 
	 */
	public void update(Order newOrder) {
//		Message msg = null;
//		long updateid = (utsg.generate(new Date()).getNanoseconds() % 100000000000000l) / 1000l;
//		if (newOrder.getClass().equals(orderContainer.getClass())) {
//
//			SingleLegOrder slo = (SingleLegOrder) newOrder;
//			//
//			String reqId = "PDT:" + orderContainer.getOrderId() + ":"
//					+ updateid;
//			// ensure that all things are copied.
//			newOrder.setOrderId(reqId);
//
//			venue.addOrderTracker(reqId, this);
//
//			if (newOrder instanceof MarketOrder) {
//				MarketOrder mo = (MarketOrder) newOrder;
//				MarketOrder loOrig = (MarketOrder) orderContainer;
//				pendingOrderContainer = mo;
//
//				mo.setOrderSide(loOrig.getOrderSide());
//				mo.setOpenQuantity(loOrig.getOpenQuantity());
//				mo.setTradInstId(loOrig.getTradInstId());
//
//				msg = mc.updateMarketOrder(reqId, loOrig.getOrderId(), venue
//						.getTdi(orderContainer.getTradInstId()), new OrderQty(
//						mo.getQuantity()),
//						loOrig.getOrderSide().getSide() == OrderSide.BUY
//								.getSide() ? new Side(Side.BUY) : new Side(
//								Side.SELL));
//			} else if (newOrder instanceof LimitOrder) {
//				LimitOrder lo = (LimitOrder) newOrder;
//				LimitOrder loOrig = (LimitOrder) orderContainer;
//				pendingOrderContainer = lo;
//
//				lo.setOrderSide(loOrig.getOrderSide());
//				lo.setOpenQuantity(loOrig.getOpenQuantity());
//				lo.setTradInstId(loOrig.getTradInstId());
//
//				msg = mc.updateLimitOrder(reqId, loOrig.getOrderId(), venue
//						.getTdi(orderContainer.getTradInstId()),
//						new Price(lo.getLimitPrice()),
//						new OrderQty(lo.getQuantity()),
//						loOrig.getOrderSide().getSide() == OrderSide.BUY
//								.getSide() ? new Side(Side.BUY) : new Side(
//								Side.SELL));
//			} else if (newOrder instanceof StopOrder) {
//				StopOrder lo = (StopOrder) newOrder;
//				StopOrder loOrig = (StopOrder) orderContainer;
//				pendingOrderContainer = lo;
//
//				lo.setOrderSide(loOrig.getOrderSide());
//				lo.setOpenQuantity(loOrig.getOpenQuantity());
//				lo.setTradInstId(loOrig.getTradInstId());
//
//				msg = mc.updateStopOrder(reqId, loOrig.getOrderId(), venue
//						.getTdi(orderContainer.getTradInstId()),
//						new StopPx(lo.getStopPrice()),
//						new OrderQty(lo.getQuantity()),
//						loOrig.getOrderSide().getSide() == OrderSide.BUY
//								.getSide() ? new Side(Side.BUY) : new Side(
//								Side.SELL));
//			}
//
//			if (msg == null)
//				return;
//			try {
//				Session.sendToTarget(msg, venue.getInterceptorLayer()
//						.getOrderSessionId());
//			} catch (SessionNotFound e) {
//				throw new RuntimeException(
//						"FATAL! FATAL! FATAL! COULD NOT FIND ORDER SESSION!");
//			}
//
//			try {
//				log.info("Distributing internally that we submitted the order. ");
//				OrderEvent oe = new OrderUpdateSubmittedEvent();
//				oe.setRefOrderId(slo.getOrderId());
//				oe.setRefOrder(slo);
//
//				fireEvent(oe);
//
//				TTTradeableInstrument tid = venue.getTdi(orderContainer
//						.getTradInstId());
//				venue.getTransportFactory()
//						.getPublisher(ETransportType.TRAD_DATA, tid.getId())
//						.send(new OrderStreamEvent(tid.getId(),
//								new TimeStamp(), oe));
//			} catch (TransportException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		} else {
//			log.warn("Cannot update order with a different type.");
//		}
	}

	public void signalSuccessfullUpdate() {
		if (pendingOrderContainer != null) {
			// making the new order the current order.
			this.pendingOrderContainer.setOrderId(orderContainer.getOrderId());

			pendingOrderContainer.setOpenQuantity(pendingOrderContainer
					.getQuantity()
					- (orderContainer.getQuantity() - orderContainer
							.getOpenQuantity()));

			this.orderContainer = pendingOrderContainer;
			pendingOrderContainer = null;
			//
			this.fireEvent(new OrderReplacedEvent());
		}
	}

	@Override
	public void cancel() {
		//
		String reqId = "CNCL:" + orderContainer.getOrderId() + ":"
				+ seqCounter++;
		String tradInstId = orderContainer.getTradInstId();

		BaseMessage bm = 
				messageFactory.OrderCancelRequest(reqId, orderContainer.getOrderId(), 
						tradInstId, orderContainer.getOrderSide(), orderContainer.getQuantity());
		if (bm != null) {
			try {
				transportPublisher.send(bm.toByteArray());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public IEventSource<OrderEvent> getOrderEventSource() {
		return event;
	}

	public OrderEvent lastState() {
		return lastState;
	}

}
