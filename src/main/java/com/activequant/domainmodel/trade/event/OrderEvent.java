package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.TimeStamp;

public abstract class OrderEvent {
	private TimeStamp creationTimeStamp;

	public TimeStamp getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(TimeStamp creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}
}
