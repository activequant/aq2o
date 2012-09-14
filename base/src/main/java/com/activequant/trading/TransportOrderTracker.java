package com.activequant.trading;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.trade.event.OrderAcceptedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelSubmittedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelledEvent;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderPendingEvent;
import com.activequant.domainmodel.trade.event.OrderReplacedEvent;
import com.activequant.domainmodel.trade.event.OrderSubmittedEvent;
import com.activequant.domainmodel.trade.event.OrderUpdateRejectedEvent;
import com.activequant.domainmodel.trade.event.OrderUpdateSubmittedEvent;
import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.MarketOrder;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.domainmodel.trade.order.SingleLegOrder;
import com.activequant.domainmodel.trade.order.StopOrder;
import com.activequant.interfaces.trading.IOrderTracker;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.utils.IEventSource;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.MessageFactory;
import com.activequant.utils.UniqueTimeStampGenerator;
import com.activequant.utils.events.Event;

public class TransportOrderTracker implements IOrderTracker {

	private SingleLegOrder orderContainer;
	private SingleLegOrder pendingOrderContainer;
	

	private SingleLegOrder nextPendingOrderContainer;
	private Event<OrderEvent> event = new Event<OrderEvent>();
	UniqueTimeStampGenerator utsg = new UniqueTimeStampGenerator();
	private Logger log = Logger.getLogger(TransportOrderTracker.class);
	private int seqCounter = 0;
	private OrderEvent lastState;
	private String internalOrderId = ""; 
	private String originalOrderId = ""; 
	private boolean cancellationPending = false; 
	public boolean isCancellationPending() {
		return cancellationPending;
	}

	private MessageFactory messageFactory;
	private IPublisher transportPublisher; 

	public TransportOrderTracker(IPublisher publisher, SingleLegOrder order) {
		this.transportPublisher = publisher; 		  
		this.orderContainer = order;
		messageFactory = new MessageFactory();
		internalOrderId = order.getOrderId();
		originalOrderId = order.getOrderId();
	}

	@Override
	public Order getOrder() {
		return orderContainer;
	}

	public void fireEvent(OrderEvent oe) {
		lastState = oe; 
		if(oe instanceof OrderCancelledEvent){
			// terminal. 
		}
		else if(oe instanceof OrderReplacedEvent)
		{
			CheckPendingOrderUpdate();
			checkPendingCancellation();
		}
		else if(oe instanceof OrderAcceptedEvent){
			CheckPendingOrderUpdate();
			checkPendingCancellation();
		}
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
			log.info("Submitting limit order:" + o.toString());
			LimitOrder lo = (LimitOrder) o;
			bm = messageFactory.orderLimitOrder(o.getOrderId(), tradInstId, lo.getQuantity(), lo
					.getLimitPrice(), lo.getOrderSide());
		} else if (o instanceof MarketOrder) {
			MarketOrder mo = (MarketOrder) o;
			bm = messageFactory.orderMktOrder(o.getOrderId()
					, tradInstId, mo.getQuantity(), mo
					.getOrderSide());
		} else if (o instanceof StopOrder) {
			StopOrder so = (StopOrder) o;
			bm = messageFactory.orderStopOrder(o.getOrderId(), tradInstId, so.getQuantity(), so
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
//
//	private void processOrderUpdateQueue(){
//		if(nextPendingOrderContainer!=null){
//			
//		}
//	}
	
	@Override
	/**
	 * Functionality is limited to updating limit order price and limit order size. 
	 * 
	 * If another order is pending, this order is scheduled for being executed AFTER the current
	 * order update is done. 
	 * 
	 * There is no queue of order updates, just ONE pending order update plus one next order update. 
	 * 
	 * Meaning, if you update a hundred times, but the first order update is not through yet while you update 100 times, 98 of those
	 * updates are dropped and only the last update will go through. 
	 * 
	 */
	public void update(Order o) {
		log.info("Updating order:" + o.toString());
		if(cancellationPending){
			log.info("Update rejected, as cancellation is pending.");
			fireEvent(new OrderUpdateRejectedEvent());
			return; 
		}

		if(lastState instanceof OrderCancelledEvent)
		{
			log.info("Update rejected, as order has been cancelled already.");
			fireEvent(new OrderUpdateRejectedEvent());
			return; 
		}
		
		if(pendingOrderContainer!=null){
			log.info("Update scheduled, as order update is pending. ");
			nextPendingOrderContainer = (SingleLegOrder)o;
			return; 
		}
		BaseMessage msg = null;		
		String originalClOrdId = internalOrderId; // this.orderContainer.getOrderId();
		String updateid = "UPDT:"+originalClOrdId + ":"+ seqCounter;
		if(originalClOrdId.startsWith("UPDT:")){
			updateid = "UPDT:"+originalClOrdId.split(":")[1]+":"+seqCounter;
		}
		seqCounter ++; 
		if (o.getClass().equals(orderContainer.getClass())) {
			// 
			// 
			String tradInstId = orderContainer.getTradInstId();
			BaseMessage bm = null;
			if (o instanceof LimitOrder) {
				LimitOrder lo = (LimitOrder) o;
				bm = messageFactory.updateLimitOrder(updateid, originalClOrdId, tradInstId, lo.getQuantity(), lo
						.getLimitPrice(), lo.getOrderSide());
			} else if (o instanceof MarketOrder) {
				MarketOrder mo = (MarketOrder) o;
				bm = messageFactory.updateMktOrder(updateid, originalClOrdId, 
						tradInstId, mo.getQuantity(), mo
						.getOrderSide());
			} else if (o instanceof StopOrder) {
				StopOrder so = (StopOrder) o;
				bm = messageFactory.updateStopOrder(updateid, originalClOrdId, tradInstId, so.getQuantity(), so
						.getStopPrice(), so.getOrderSide());
			}
			if (bm != null) {
				try {
					log.info("Sending order update for " + o.toString());
					pendingOrderContainer = (SingleLegOrder)o; 
					pendingOrderContainer.setOrderId(updateid);
					transportPublisher.send(bm.toByteArray());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}			
			// 
			fireEvent(new OrderUpdateSubmittedEvent());
		} else {
			log.warn("Cannot update order with a different type.");
		}
	}

	public void CheckPendingOrderUpdate() {
		if (pendingOrderContainer != null) {
			// making the new order the current order.
			internalOrderId = this.pendingOrderContainer.getOrderId();
			this.pendingOrderContainer.setOrderId(originalOrderId);
			pendingOrderContainer.setOpenQuantity(pendingOrderContainer
					.getQuantity()
					- (orderContainer.getQuantity() - orderContainer
							.getOpenQuantity()));

			this.orderContainer = pendingOrderContainer;
			pendingOrderContainer = null;
			//
			
			// DO 
			if(cancellationPending)
				return;
			
			// check if there is a next pending order container. 
			if(nextPendingOrderContainer!=null)
				update(nextPendingOrderContainer);
		}
	}
	
	public void checkPendingCancellation(){
		if(cancellationPending){
			sendCancel();
			cancellationPending = false; 
		}
	}

	@Override
	public void cancel() {
		if(cancellationPending)
			return;
		cancellationPending = true;
		// check the last state. 
		if(  ((lastState instanceof OrderAcceptedEvent) || (lastState instanceof OrderReplacedEvent)) 
				&& pendingOrderContainer==null){ 
			sendCancel();
		}
	}

	private void sendCancel(){
		//
		String reqId = "CNCL:" + originalOrderId + ":"
				+ seqCounter;
		seqCounter ++; 
		String tradInstId = orderContainer.getTradInstId();

		BaseMessage bm = 
				messageFactory.OrderCancelRequest(reqId, internalOrderId, 
						tradInstId, orderContainer.getOrderSide(), orderContainer.getQuantity());
		if (bm != null) {
			try {
				log.info("Sending cancellation: "+ orderContainer.toString());
				transportPublisher.send(bm.toByteArray());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
		fireEvent(new OrderCancelSubmittedEvent());
	}
	
	@Override
	public IEventSource<OrderEvent> getOrderEventSource() {
		return event;
	}

	public OrderEvent lastState() {
		return lastState;
	}
	
	public SingleLegOrder getPendingOrder() {
		return pendingOrderContainer;
	}

}
