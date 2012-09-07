package com.activequant.trading;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderReplacedEvent;
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
	private Event<OrderEvent> event = new Event<OrderEvent>();
	UniqueTimeStampGenerator utsg = new UniqueTimeStampGenerator();
	private Logger log = Logger.getLogger(TransportOrderTracker.class);
	private int seqCounter = 0;
	private OrderEvent lastState;
	private String internalOrderId = ""; 
	private String originalOrderId = ""; 

	private MessageFactory messageFactory;
	private IPublisher transportPublisher; 
	private Order nextPendingOrderContainer; 

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
	 */
	public void update(Order o) {
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
					pendingOrderContainer = (SingleLegOrder)o; 
					pendingOrderContainer.setOrderId(updateid);
					transportPublisher.send(bm.toByteArray());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}					
		} else {
			log.warn("Cannot update order with a different type.");
		}
	}

	public void signalSuccessfullUpdate() {
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
			this.fireEvent(new OrderReplacedEvent());
		}
	}

	@Override
	public void cancel() {
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
