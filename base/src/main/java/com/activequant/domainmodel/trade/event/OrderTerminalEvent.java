package com.activequant.domainmodel.trade.event;


public class OrderTerminalEvent extends OrderEvent {
	public OrderTerminalEvent(){
		super(OrderTerminalEvent.class.getCanonicalName());
	}
	public String toString(){
		return "Order " + super.getRefOrderId() + " in terminal state. Done."; 
	}
}
