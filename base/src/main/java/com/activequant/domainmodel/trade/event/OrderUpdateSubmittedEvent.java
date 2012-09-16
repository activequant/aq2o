package com.activequant.domainmodel.trade.event;


public class OrderUpdateSubmittedEvent extends OrderEvent {

	public OrderUpdateSubmittedEvent(){
		super(OrderUpdateSubmittedEvent.class.getCanonicalName());
	}

	public String toString(){
		String s = "";
		if(getRefOrder()!=null)
			s = getRefOrder().toString();
		return "Order update of  " + s + " submitted."; 
	}
	
}
