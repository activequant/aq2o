package com.activequant.trading;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.trade.event.OrderAcceptedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelSubmittedEvent;
import com.activequant.domainmodel.trade.event.OrderCancellationRejectedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelledEvent;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.domainmodel.trade.event.OrderRejectedEvent;
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

/**
 * not thread safe. permit access from one thread only.
 * 
 * @author GhostRider
 * 
 */
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
	//
	private boolean cancellationPending = false;

	//
	private boolean workingState = false;
	private boolean terminalState = false;
	private boolean submitted = false;

	public boolean isCancellationPending() {
		return cancellationPending;
	}
	public boolean isInTerminalState(){
		return terminalState;
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

	/**
	 * Called from outside and inside this class.
	 * 
	 * @param oe
	 */
	public void fireEvent(OrderEvent oe) {
		lastState = oe;
		log.info("Received an order event: " + oe);
		if (oe instanceof OrderSubmittedEvent) {
			submitted = true;
			workingState = false;
		} else if (oe instanceof OrderRejectedEvent) {
			terminalState = true;
			workingState = false;
		} else if (oe instanceof OrderCancelledEvent) {
			terminalState = true;
			workingState = false;
		} else if (oe instanceof OrderReplacedEvent
				|| oe instanceof OrderAcceptedEvent
				|| oe instanceof OrderCancellationRejectedEvent
				|| oe instanceof OrderUpdateRejectedEvent) {
			// ok, order is working again.
			workingState = true;

			if (oe instanceof OrderReplacedEvent)
				this.orderUpdateSucceeded();
			else if(oe instanceof OrderUpdateRejectedEvent)
				this.orderUpdateFailed();
			//
			if (oe instanceof OrderCancellationRejectedEvent)
				cancellationPending = false;

			// first check if there is a scheduled cancellation ...
			checkPendingCancellation();
			// then check if there is a pending order update.
			// we also check if we are still in a working state, as there could
			// have been a cancellation
			// indeed!!!
			if (workingState)
				checkPendingOrderUpdate();

		} else if (oe instanceof OrderCancelSubmittedEvent) {
			// we have to wait for a response now, so mark as nonworking.
			workingState = false;
		} else if (oe instanceof OrderUpdateSubmittedEvent) {
			// we have to wait for a response now, so mark as nonworking.
			workingState = false;
		}
		else if(oe instanceof OrderFillEvent){
			// 
			fill((OrderFillEvent)oe);
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

		if (submitted) {
			log.info("order has been submitted already.");
			return;
		}

		if (terminalState) {
			log.warn("Order is in terminal state, cannot resubmit.");
			return;
		}

		Order o = orderContainer;
		String tradInstId = orderContainer.getTradInstId();
		BaseMessage bm = null;
		if (o instanceof LimitOrder) {
			log.info("Submitting limit order:" + o.toString());
			LimitOrder lo = (LimitOrder) o;
			bm = messageFactory.orderLimitOrder(o.getOrderId(), tradInstId,
					lo.getQuantity(), lo.getLimitPrice(), lo.getOrderSide());
		} else if (o instanceof MarketOrder) {
			MarketOrder mo = (MarketOrder) o;
			bm = messageFactory.orderMktOrder(o.getOrderId(), tradInstId,
					mo.getQuantity(), mo.getOrderSide());
		} else if (o instanceof StopOrder) {
			StopOrder so = (StopOrder) o;
			bm = messageFactory.orderStopOrder(o.getOrderId(), tradInstId,
					so.getQuantity(), so.getStopPrice(), so.getOrderSide());
		}
		if (bm != null) {
			try {
				transportPublisher.send(bm.toByteArray());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		fireEvent(new OrderSubmittedEvent());

	}

	//
	// private void processOrderUpdateQueue(){
	// if(nextPendingOrderContainer!=null){
	//
	// }
	// }

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
	 * updates are dropped and only the last update will go through. (good for HFT)
	 * 
	 */
	public void update(Order o) {

		if (terminalState) {
			log.info("Cannot update order as order is in terminal state.");
			fireEvent(new OrderUpdateRejectedEvent());
			return;
		}

		log.info("Updating order:" + orderContainer.toString() + " to "
				+ o.toString());

		if (!workingState) {
			log.info("Cannot update order as order is not in a working state, setting it as pending.");
			// mark for update.
			nextPendingOrderContainer = (SingleLegOrder) o;
			return;
		}

		String originalClOrdId = internalOrderId; // this.orderContainer.getOrderId();
		String updateid = "UPDT:" + originalClOrdId + ":" + seqCounter;
		if (originalClOrdId.startsWith("UPDT:")) {
			updateid = "UPDT:" + originalClOrdId.split(":")[1] + ":"
					+ seqCounter;
		}
		seqCounter++;
		if (o.getClass().equals(orderContainer.getClass())) {
			//
			//
			String tradInstId = orderContainer.getTradInstId();
			BaseMessage bm = null;
			if (o instanceof LimitOrder) {
				LimitOrder lo = (LimitOrder) o;
				bm = messageFactory.updateLimitOrder(updateid, originalClOrdId,
						tradInstId, lo.getQuantity(), lo.getLimitPrice(),
						lo.getOrderSide());
			} else if (o instanceof MarketOrder) {
				MarketOrder mo = (MarketOrder) o;
				bm = messageFactory.updateMktOrder(updateid, originalClOrdId,
						tradInstId, mo.getQuantity(), mo.getOrderSide());
			} else if (o instanceof StopOrder) {
				StopOrder so = (StopOrder) o;
				bm = messageFactory.updateStopOrder(updateid, originalClOrdId,
						tradInstId, so.getQuantity(), so.getStopPrice(),
						so.getOrderSide());
			}
			if (bm != null) {
				try {
					log.info("Sending order update for " + o.toString());
					pendingOrderContainer = (SingleLegOrder) o;
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
			fireEvent(new OrderUpdateRejectedEvent());
		}
	}

	private void fill(OrderFillEvent oe){
		if(oe.getLeftQuantity()==0.0){
			// fully done. 
			workingState = false;
			terminalState = true; 
			
		}
	}
	
	private void orderUpdateSucceeded() {
		// making the new order the current order.
		internalOrderId = this.pendingOrderContainer.getOrderId();
		this.pendingOrderContainer.setOrderId(originalOrderId);
		pendingOrderContainer.setOpenQuantity(pendingOrderContainer
				.getQuantity()
				- (orderContainer.getQuantity() - orderContainer
						.getOpenQuantity()));

		this.orderContainer = pendingOrderContainer;
		pendingOrderContainer = null;
	}
	
	private void orderUpdateFailed() {
		// making the new order the current order.		
		pendingOrderContainer = null;
	}

	private void checkPendingOrderUpdate() {
		// check if there is a next pending order container.
		if (nextPendingOrderContainer != null) {
			update(nextPendingOrderContainer);
			nextPendingOrderContainer = null;
		}
	}

	/**
	 * checks if there is a cancellation pending and if so, it send it.
	 */
	private void checkPendingCancellation() {
		if (cancellationPending) {
			sendCancel();
		}
	}

	@Override
	public void cancel() {

		if (terminalState) {
			log.info("Cannot cancel order as order is in terminal state.");
			fireEvent(new OrderCancellationRejectedEvent());
			return;
		} else if (!workingState) {
			log.info("Cannot cancel order as order is not in a working state (but waiting for response), marking order as to be cancelled. ");
			cancellationPending = true;
			return;
		} else {
			sendCancel();
		}

	}

	private void sendCancel() {

		// ok, we are working out a pending cancellation.
		cancellationPending = false;

		fireEvent(new OrderCancelSubmittedEvent());
		log.info("Cancellation called for " + internalOrderId + ". "
				+ lastState);
		//
		String reqId = "CNCL:" + originalOrderId + ":" + seqCounter;
		seqCounter++;
		String tradInstId = orderContainer.getTradInstId();
		log.info("Sending cancel with cancel reqId:" + reqId);

		BaseMessage bm = messageFactory.OrderCancelRequest(reqId,
				internalOrderId, tradInstId, orderContainer.getOrderSide(),
				orderContainer.getQuantity());
		if (bm != null) {
			try {
				log.info("Sending cancellation: " + orderContainer.toString());
				transportPublisher.send(bm.toByteArray());

			} catch (Exception e) {
				// DIRTY.
				log.warn("Exception!!", e);
				fireEvent(new OrderCancellationRejectedEvent());
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

	public SingleLegOrder getPendingOrder() {
		return pendingOrderContainer;
	}

}
