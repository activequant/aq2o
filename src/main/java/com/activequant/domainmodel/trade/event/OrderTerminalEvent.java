package com.activequant.domainmodel.trade.event;


public class OrderTerminalEvent extends OrderEvent {

	public String toString(){
		return "Order " + super.getRefOrderId() + " in terminal state. Done."; 
	}
}
