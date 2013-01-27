package com.activequant.domainmodel.trade.event;

import com.activequant.utils.UniqueTimeStampGenerator;


public class OrderTerminalEvent extends OrderEvent {
	public OrderTerminalEvent(){
		super(OrderTerminalEvent.class.getCanonicalName());
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}
	
	@Override
	public String getId() {
		return "OTE."+nullSafe(getTimeStamp());
	}

	
	public OrderTerminalEvent(Class parentClass){
		super(parentClass.getCanonicalName());
	}
	
	
	public String toString(){
		return "Order " + super.getRefOrderId() + " in terminal state. Done."; 
	}
}
