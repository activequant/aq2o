package com.activequant.domainmodel.trade.event;

import com.activequant.utils.UniqueTimeStampGenerator;

public class OrderReplacedEvent extends OrderEvent {

	public OrderReplacedEvent(){
		super(OrderReplacedEvent.class.getCanonicalName());
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}
	@Override
	public String getId() {
		return "ORE."+nullSafe(getTimeStamp());
	}

	public String toString(){
		return "Order " + super.getRefOrderId() + " replaced."; 
	}
	
}
