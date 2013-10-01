package com.activequant.domainmodel.trade.event;

import com.activequant.utils.UniqueTimeStampGenerator;


public class OrderUpdateSubmittedEvent extends OrderEvent {

	public OrderUpdateSubmittedEvent(){
		super(OrderUpdateSubmittedEvent.class.getCanonicalName());
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}
	@Override
	public String getId() {
		return "OUSE."+nullSafe(getTimeStamp());
	}


	public String toString(){
		String s = "";
		if(getRefOrder()!=null)
			s = getRefOrder().toString();
		return "Order update of  " + s + " submitted."; 
	}
	
}
