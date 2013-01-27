package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.annotations.Property;
import com.activequant.utils.UniqueTimeStampGenerator;


public class OrderCancelledEvent extends OrderTerminalEvent {
	private String cancellationMessage;

	public OrderCancelledEvent(){
		super(OrderCancelledEvent.class);
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}
	@Override
	public String getId() {
		return "OCE."+nullSafe(getTimeStamp());
	}
	
	
	@Property
	public String getCancellationMessage() {
		return cancellationMessage;
	}

	public void setCancellationMessage(String cancellationMessage) {
		this.cancellationMessage = cancellationMessage;
	}

	public String toString(){
		return "Order " + super.getRefOrderId() + " cancelled: " + cancellationMessage; 
	}
}
