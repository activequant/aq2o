package com.activequant.domainmodel.trade.event;

public class OrderReplacedEvent extends OrderEvent {

	public OrderReplacedEvent(){
		super(OrderReplacedEvent.class.getCanonicalName());
	}
	
	public String toString(){
		return "Order " + super.getRefOrderId() + " replaced."; 
	}
	
}
