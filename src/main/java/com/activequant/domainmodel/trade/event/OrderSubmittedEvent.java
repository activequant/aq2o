package com.activequant.domainmodel.trade.event;


public class OrderSubmittedEvent extends OrderEvent {
	

	public OrderSubmittedEvent(){
		super(OrderSubmittedEvent.class.getCanonicalName());
	}
	
	public String toString(){
		return "Order " + super.getRefOrderId() + " submitted."; 
	}
	
}
