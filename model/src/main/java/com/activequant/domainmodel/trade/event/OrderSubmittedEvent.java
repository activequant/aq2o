package com.activequant.domainmodel.trade.event;

import com.activequant.utils.UniqueTimeStampGenerator;


public class OrderSubmittedEvent extends OrderEvent {
	
	public OrderSubmittedEvent(){
		super(OrderSubmittedEvent.class.getCanonicalName());
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}
	@Override
	public String getId() {
		return "OSE."+nullSafe(getTimeStamp());
	}

	public String toString(){
		String r =  "Order " + super.getRefOrderId() + " submitted.";
		if(refOrder!=null)
			r += " "+ refOrder.toString();
		return r; 
	}
	
}
