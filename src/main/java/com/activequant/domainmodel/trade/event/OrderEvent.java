package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.TimeStamp;

public abstract class OrderEvent {
	private TimeStamp creationTimeStamp;
	private String refOrderId; 

	public TimeStamp getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(TimeStamp creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	public String getRefOrderId() {
		return refOrderId;
	}

	public void setRefOrderId(String refOrderId) {
		this.refOrderId = refOrderId;
	}
}
