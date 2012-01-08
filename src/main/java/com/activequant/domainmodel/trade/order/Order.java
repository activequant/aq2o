package com.activequant.domainmodel.trade.order;

import com.activequant.domainmodel.Date8Time6;

public class Order {
	private Date8Time6 creationTimeStamp;
	private Date8Time6 workingTimeStamp; 

	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date8Time6 getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(Date8Time6 creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}
	public Date8Time6 getWorkingTimeStamp() {
		return workingTimeStamp;
	}

	public void setWorkingTimeStamp(Date8Time6 workingTimeStamp) {
		this.workingTimeStamp = workingTimeStamp;
	}

}
