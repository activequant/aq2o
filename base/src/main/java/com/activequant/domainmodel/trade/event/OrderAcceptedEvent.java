package com.activequant.domainmodel.trade.event;

import com.activequant.utils.UniqueTimeStampGenerator;




public class OrderAcceptedEvent extends OrderEvent {
	
	
	public OrderAcceptedEvent(){
		super(OrderAcceptedEvent.class.getCanonicalName());
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}
	@Override
	public String getId() {
		return "OAE."+nullSafe(getTimeStamp());
	}
	
	// 
	public String toString(){
		return "Order " + super.getRefOrderId() + " accepted."; 
	}
	
}
