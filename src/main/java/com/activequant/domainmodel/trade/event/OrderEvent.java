package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.Date8Time6;

public abstract class OrderEvent {
	private Date8Time6 creationTimeStamp;

	public Date8Time6 getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(Date8Time6 creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}
}
