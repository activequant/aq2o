package com.activequant.domainmodel.trade.event;


public class OrderUpdateSubmittedEvent extends OrderEvent {

	public OrderUpdateSubmittedEvent(){
		super(OrderUpdateSubmittedEvent.class.getCanonicalName());
	}

	public String toString(){
		return "Order update of " + super.getRefOrderId() + " submitted."; 
	}
	
}
