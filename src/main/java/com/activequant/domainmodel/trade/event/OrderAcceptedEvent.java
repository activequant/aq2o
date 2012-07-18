package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;



public class OrderAcceptedEvent extends OrderEvent {
	
	
	public OrderAcceptedEvent(){
		super(OrderAcceptedEvent.class.getCanonicalName());
	}
	
	// 
	public String toString(){
		return "Order " + super.getRefOrderId() + " accepted."; 
	}

}
