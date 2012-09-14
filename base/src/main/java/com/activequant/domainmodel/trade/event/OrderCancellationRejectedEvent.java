package com.activequant.domainmodel.trade.event;


public class OrderCancellationRejectedEvent extends OrderEvent {
	private String reason = "";

	public OrderCancellationRejectedEvent(){
		super(OrderCancellationRejectedEvent.class.getCanonicalName());
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	} 
	
	

	public String toString(){
		return "Order cancellation " + super.getRefOrderId() + " rejected: " + reason; 
	}
}
