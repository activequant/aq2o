package com.activequant.domainmodel.trade.event;


public class OrderUpdateRejectedEvent extends OrderEvent {
	private String reason = "";

	public OrderUpdateRejectedEvent(){
		super(OrderUpdateRejectedEvent.class.getCanonicalName());
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	} 
	
	

	public String toString(){
		return "Order " + super.getRefOrderId() + " rejected: " + reason; 
	}
}
