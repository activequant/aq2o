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
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.interfaces.utils.IEventSource;
import com.activequant.messages.AQMessages;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.AQMessages.OrderRejected;
import com.activequant.messages.Marshaller;
import com.activequant.messages.MessageFactory;
import com.activequant.utils.UniqueTimeStampGenerator;
import com.activequant.utils.events.Event;
import com.google.protobuf.InvalidProtocolBufferException;

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

	private MessageFactory messageFactory;
	private IPublisher transportPublisher;
	private IReceiver transportReceiver;
	private Marshaller marshaller = new Marshaller();

	public TransportOrderTracker(IPublisher publisher, IReceiver receiver,
			SingleLegOrder order) {

		this.transportPublisher = publisher;
		this.transportReceiver = receiver;

		// register the byte[] handler.
		receiver.getRawEvent().addEventListener(new IEventListener<byte[]>() {
			@Override
			public void eventFired(byte[] arg0) {
				handle(arg0);
			}
		});

		this.orderContainer = order;
		order.setOpenQuantity(order.getQuantity());
		messageFactory = new MessageFactory();
		internalOrderId = order.getOrderId();
		originalOrderId = order.getOrderId();

		//
	}

	@Override
	public Order getOrder() {
		return orderContainer;
	}

	/**
	 * Called from outside and inside this class. Routes an event within this
	 * virtual machine (not inter-machine communication)
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
			else if (oe instanceof OrderUpdateRejectedEvent)
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
		} else if (oe instanceof OrderFillEvent) {
			//
			fill((OrderFillEvent) oe);
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
		if (o instanceof MarketOrder) {
			MarketOrder mo = (MarketOrder) o;
			bm = messageFactory.orderMktOrder(o.getOrderId(), tradInstId,
					mo.getQuantity(), mo.getOrderSide());
		} else if (o instanceof LimitOrder) {
			log.info("Submitting limit order:" + o.toString());
			LimitOrder lo = (LimitOrder) o;
			bm = messageFactory.orderLimitOrder(o.getOrderId(), tradInstId,
					lo.getQuantity(), lo.getLimitPrice(), lo.getOrderSide());

		} else if (o instanceof StopOrder) {
			StopOrder so = (StopOrder) o;
			bm = messageFactory.orderStopOrder(o.getOrderId(), tradInstId,
					so.getQuantity(), so.getStopPrice(), so.getOrderSide());
		}
		if (bm != null) {
			try {
				transportPublisher.send(bm.toByteArray());
			} catch (Exception e) {
				log.warn("Error while sending message: ", e);
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
					// finally notify the event listeners.
					fireEvent(new OrderUpdateSubmittedEvent());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			//
		} else {
			log.warn("Cannot update order with a different type.");
			fireEvent(new OrderUpdateRejectedEvent());
		}
	}

	private void fill(OrderFillEvent oe) {
		if (oe.getLeftQuantity() == 0.0) {
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

	/**
	 * Cancels an order on the event bus.
	 * 
	 */
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

		//
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
				fireEvent(new OrderCancelSubmittedEvent());
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

	/**
	 * Handles incoming raw byte messages. Rohes Fleisch in ihrer reinsten Form.
	 * Mit Terijaki Sauce.
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
			//System.out.println(bm);
			switch (bm.getType()) {
			case SECURITY_STATUS: {
				AQMessages.SecurityStatus os = ((AQMessages.SecurityStatus) bm
						.getExtension(AQMessages.SecurityStatus.cmd));
				// handle(os);
				break;
			}
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
			case ORD_CNCL_REJ: {
				log.info("Order cancellation rejected.");
				AQMessages.OrderCancelReject ocr = ((AQMessages.OrderCancelReject) bm
						.getExtension(AQMessages.OrderCancelReject.cmd));
				handle(ocr);
				break;
			}
			case ORD_UPD_REJECTED: {
				log.info("Order update rejected.");
				AQMessages.OrderUpdateRejected ocr = ((AQMessages.OrderUpdateRejected) bm
						.getExtension(AQMessages.OrderUpdateRejected.cmd));
				handle(ocr);
				break;
			}
			case ORD_UPDATE_SUBMITTED: {
				log.info("Order update submitted.");
				AQMessages.OrderUpdateSubmitted ocr = ((AQMessages.OrderUpdateSubmitted) bm
						.getExtension(AQMessages.OrderUpdateSubmitted.cmd));
				handle(ocr);
				break;
			}
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
			case EXECUTION_REPORT2:
				AQMessages.ExecutionReport2 er = ((AQMessages.ExecutionReport2) bm
						.getExtension(AQMessages.ExecutionReport2.cmd));
				handle(er);
				break;
			}

		} catch (InvalidProtocolBufferException e) {
			log.warn("Could not demarshall message.");
		}

	}

	// private void handle(AQMessages.SecurityStatus sa) {
	// //
	// MarketOpen mo = new MarketOpen();
	// mo.setTdiId(sa.getTdiId());
	// mo.setText(sa.getStatus());
	// this.marketStateEvent.fire(mo);
	// }

	private void handle(AQMessages.OrderSubmitted oa) {
		// should do something about UPD messages.
		String ordId = oa.getClOrdId();
		if (ordId.startsWith("UPDT")) {
			ordId = ordId.split(":")[1];
		}

		// let's check if this order event is for us.
		if (ordId.equals(this.originalOrderId)) {
			OrderSubmittedEvent oae = new OrderSubmittedEvent();
			oae.setRefOrderId(ordId);
			oae.setRefOrder(this.getOrder());
			fireEvent(oae);

		}
	}

	private void handle(OrderRejected or) {
		String orderId = or.getClOrdId();
		if (orderId.startsWith("UPDT:")) {
			// alright, let's split it.
			orderId = orderId.split(":")[1];
		}
		if (orderId.equals(originalOrderId)) {
			// ok, it's for us.
			log.info("Order rejected: " + or.getClOrdId());
			OrderRejectedEvent oae = new OrderRejectedEvent();
			oae.setRefOrderId(orderId);
			oae.setRefOrder(getOrder());
			oae.setReason(or.getReason());
			fireEvent(oae);
		}
	}

	private void handle(AQMessages.OrderAccepted oa) {

		String orderId = oa.getClOrdId();
		if (orderId.equals(originalOrderId)) {
			log.info("Order accepted: " + oa.getClOrdId());
			OrderAcceptedEvent oae = new OrderAcceptedEvent();
			oae.setRefOrderId(oa.getClOrdId());
			oae.setRefOrder(getOrder());
			fireEvent(oae);
		}
	}

	private void handle(AQMessages.OrderUpdateRejected oa) {

		String orderId = oa.getClOrdId();
		if (orderId.equals(originalOrderId)) {
			log.info("Order accepted: " + oa.getClOrdId());
			OrderUpdateRejectedEvent oae = new OrderUpdateRejectedEvent();
			oae.setRefOrderId(oa.getClOrdId());
			oae.setRefOrder(getOrder());
			oae.setReason(oa.getReason());
			fireEvent(oae);
		}
	}

	/**
	 * 
	 * Translates wire execution reports to internal OrderFillEvents.
	 * 
	 * @param er
	 */

	private void handle(AQMessages.ExecutionReport2 er) {

		String orderId = er.getClOrdId();

		if (orderId.startsWith("UPDT:")) {
			// alright, let's split it.
			orderId = orderId.split(":")[1];
		}
		if (orderId.equals(originalOrderId)) {

			log.info("Execution report: " + er.getClOrdId());

			OrderFillEvent ofe = new OrderFillEvent();

			Double cumQty = er.getQty();
			Double avgPx = er.getPrice();

			String side = er.getSide();
			ofe.setSide(side);

			ofe.setFillPrice(er.getPrice());
			ofe.setFillAmount(cumQty);
			ofe.setOptionalInstId(er.getTdiId());
			ofe.setRefOrderId(orderId);
			ofe.setRefOrder(getOrder());
			fireEvent(ofe);

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
		if (orderId.equals(originalOrderId)) {

			log.info("Cancel received for " + orderId);
			//
			OrderCancelledEvent oce = new OrderCancelledEvent();
			oce.setRefOrderId(orderId);
			oce.setRefOrder(getOrder());
			fireEvent(oce);
		} else {
			log.info("Could not find order tracker " + oc.getClOrdId());
		}
	}

	private void handle(AQMessages.OrderUpdated ou) {
		log.info("Order updated: " + ou.getClOrdId());
		String orderId = ou.getClOrdId();
		if (orderId.startsWith("UPDT:")) {
			// alright, let's split it.
			orderId = orderId.split(":")[1];
		}
		if (orderId.equals(originalOrderId)) {
			//
			OrderReplacedEvent ore = new OrderReplacedEvent();
			ore.setRefOrderId(orderId);
			ore.setRefOrder(getPendingOrder());
			fireEvent(ore);

		}

	}

	private void handle(AQMessages.OrderCancelReject ocr) {
		String orderId = ocr.getClOrdId();
		if (orderId.startsWith("CNCL:")) {
			// alright, let's split it.
			orderId = orderId.split(":")[1];
		}
		if (orderId.equals(originalOrderId)) {
			//
			log.info("Order cancellation rejected of " + ocr.getClOrdId());
			OrderCancellationRejectedEvent oce = new OrderCancellationRejectedEvent();
			oce.setReason(ocr.getClxRejReason());
			oce.setRefOrderId(orderId);
			oce.setRefOrder(getOrder());
			fireEvent(oce);
		}
	}

	private void handle(AQMessages.OrderUpdateSubmitted ocr) {
		String orderId = ocr.getClOrdId();
		if (orderId.startsWith("UPDT:")) {
			// alright, let's split it.
			orderId = orderId.split(":")[1];
		}
		if (orderId.equals(originalOrderId)) {
			//
			OrderUpdateSubmittedEvent oce = new OrderUpdateSubmittedEvent();
			oce.setRefOrderId(orderId);
			oce.setRefOrder(getOrder());
			fireEvent(oce);
		}
	}

	
	
	public boolean isCancellationPending() {
		return cancellationPending;
	}

	public boolean isInTerminalState() {
		return terminalState;
	}

}
