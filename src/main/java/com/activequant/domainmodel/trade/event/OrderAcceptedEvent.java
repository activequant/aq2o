package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;



public class OrderAcceptedEvent extends OrderEvent {
	
	
	public OrderAcceptedEvent(){
		super(OrderAcceptedEvent.class.getCanonicalName());
	}
	
	// 
	public String toString(){
		return "Order " + super.getRefOrderId() + " accepted."; 
	}

}
