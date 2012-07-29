package com.activequant.trading.virtual;

import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderSubmittedEvent;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.interfaces.trading.IExchange;
import com.activequant.interfaces.trading.IOrderTracker;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.interfaces.utils.IEventSource;
import com.activequant.utils.events.Event;

class VirtualOrderTracker implements IOrderTracker {

	private final IExchange exchange;
	private final Order order;
	private final Event<OrderEvent> event = new Event<OrderEvent>();
	private final String venueAssignedId;
	private OrderEvent lastState; 

	VirtualOrderTracker(IExchange exchange, String venueAssignedId, Order order) {
		this.exchange = exchange;
		this.order = order;
		this.venueAssignedId = venueAssignedId;  
		event.addEventListener(new IEventListener<OrderEvent>() {				
			@Override
			public void eventFired(OrderEvent event) {
				lastState = event;
			}
		});
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
		oe.setTimeStamp(exchange.currentExchangeTime()); 
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
	
	public OrderEvent lastState() {
		return lastState;
	}

}
