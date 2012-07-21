package com.activequant.domainmodel.trade.order;

import com.activequant.domainmodel.TimeStamp;

public class Order {
	private TimeStamp creationTimeStamp;
	private TimeStamp workingTimeStamp; 

	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public TimeStamp getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(TimeStamp creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}
	public TimeStamp getWorkingTimeStamp() {
		return workingTimeStamp;
	}

	public void setWorkingTimeStamp(TimeStamp workingTimeStamp) {
		this.workingTimeStamp = workingTimeStamp;
	}

}
