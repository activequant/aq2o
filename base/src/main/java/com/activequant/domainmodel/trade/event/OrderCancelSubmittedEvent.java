package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.annotations.Property;
import com.activequant.utils.UniqueTimeStampGenerator;


public class OrderCancelSubmittedEvent extends OrderTerminalEvent {
	private String cancellationMessage;

	public OrderCancelSubmittedEvent(){
		super(OrderCancelSubmittedEvent.class);
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}
	@Override
	public String getId() {
		return "OCSE."+nullSafe(getTimeStamp());
	}
	
	@Property
	public String getCancellationMessage() {
		return cancellationMessage;
	}

	public void setCancellationMessage(String cancellationMessage) {
		this.cancellationMessage = cancellationMessage;
	}

	public String toString(){
		return "Order " + super.getRefOrderId() + " cancellation submitted."; 
	}
}
