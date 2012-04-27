package com.activequant.domainmodel.trade.event;



public class OrderAcceptedEvent extends OrderEvent {
	
	
	// 
	public String toString(){
		return "Order " + super.getRefOrderId() + " accepted."; 
	}
}
