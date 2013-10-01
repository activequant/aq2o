package com.activequant.domainmodel.trade.event;

import com.activequant.utils.UniqueTimeStampGenerator;


public class OrderPendingEvent extends OrderTerminalEvent {

	public OrderPendingEvent(){
		super(OrderPendingEvent.class);
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}
	@Override
	public String getId() {
		return "OPE."+nullSafe(getTimeStamp());
	}
	
	
	

	public String toString(){
		return "Order " + super.getRefOrderId() + " pending. "; 
	}
	
}
