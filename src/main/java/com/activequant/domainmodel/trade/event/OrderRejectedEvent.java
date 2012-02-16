package com.activequant.domainmodel.trade.event;


public class OrderRejectedEvent extends OrderEvent {
	private String reason = "";

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	} 
	
}
