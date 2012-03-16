package com.activequant.domainmodel.trade.event;

public class OrderReplacedEvent extends OrderEvent {


	public String toString(){
		return "Order " + super.getRefOrderId() + " replaced."; 
	}
	
}
