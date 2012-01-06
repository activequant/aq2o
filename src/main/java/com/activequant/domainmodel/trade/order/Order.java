package com.activequant.domainmodel.trade.order;

import com.activequant.domainmodel.Date8Time6;

public class Order {
	private Date8Time6 creationTimeStamp;
	private boolean anonymousOrder = false;

	public boolean isAnonymousOrder() {
		return anonymousOrder;
	}

	public void setAnonymousOrder(boolean anonymousOrder) {
		this.anonymousOrder = anonymousOrder;
	}

	public Date8Time6 getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(Date8Time6 creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}
}
