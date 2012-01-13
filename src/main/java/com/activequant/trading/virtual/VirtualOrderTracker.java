package com.activequant.trading.virtual;

import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderSubmittedEvent;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.trading.IOrderTracker;
import com.activequant.utils.events.Event;
import com.activequant.utils.events.IEventSource;

class VirtualOrderTracker implements IOrderTracker {

	private final IExchange exchange;
	private final Order order;
	private final Event<OrderEvent> event = new Event<OrderEvent>();
	private final String venueAssignedId; 

	VirtualOrderTracker(IExchange exchange, String venueAssignedId, Order order) {
		this.exchange = exchange;
		this.order = order;
		this.venueAssignedId = venueAssignedId;  
	}
	
	@Override
	public Order getOrder() {
		return order;
	}

	@Override
	public String getVenueAssignedId() {
		return venueAssignedId;  
	}

	@Override
	public void submit() {
		OrderEvent oe = new OrderSubmittedEvent();	
		oe.setCreationTimeStamp(exchange.currentExchangeTime()); 
		event.fire(oe);
		// 
	}

	@Override
	public void update(Order newOrder) {
		// this.order.set = newOrder;
	}

	@Override
	public void cancel() {

	}

	@Override
	public IEventSource<OrderEvent> getOrderEventSource() {
		return event;
	}

}
