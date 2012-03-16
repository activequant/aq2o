package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.trade.order.Order;


public class OrderAcceptedEvent extends OrderEvent {
	
	
	// 
	public String toString(){
		return "Order " + super.getRefOrderId() + " accepted."; 
	}
}
