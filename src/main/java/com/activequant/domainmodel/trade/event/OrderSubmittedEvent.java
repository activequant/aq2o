package com.activequant.domainmodel.trade.event;


public class OrderSubmittedEvent extends OrderEvent {
	

	public String toString(){
		return "Order " + super.getRefOrderId() + " submitted."; 
	}
	
}
