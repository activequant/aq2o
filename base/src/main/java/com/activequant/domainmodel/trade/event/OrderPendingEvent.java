package com.activequant.domainmodel.trade.event;

import com.activequant.utils.UniqueTimeStampGenerator;


public class OrderPendingEvent extends OrderTerminalEvent {
	private String cancellationMessage;

	public OrderPendingEvent(){
		super(OrderPendingEvent.class);
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}
	@Override
	public String getId() {
		return "OPE."+nullSafe(getTimeStamp());
	}
	
	
	public String getCancellationMessage() {
		return cancellationMessage;
	}

	public void setCancellationMessage(String cancellationMessage) {
		this.cancellationMessage = cancellationMessage;
	}
	
	

	public String toString(){
		return "Order " + super.getRefOrderId() + " pending: " + cancellationMessage; 
	}
	
}
