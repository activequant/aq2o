package com.activequant.domainmodel.trade.event;


public class OrderSubmittedEvent extends OrderEvent {
	
	public OrderSubmittedEvent(){
		super(OrderSubmittedEvent.class.getCanonicalName());
	}
	
	public String toString(){
		String r =  "Order " + super.getRefOrderId() + " submitted.";
		if(refOrder!=null)
			r += " "+ refOrder.toString();
		return r; 
	}
	
}
