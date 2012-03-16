package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.order.Order;

public abstract class OrderEvent {
	private TimeStamp creationTimeStamp;
	private String refOrderId; 
	private String optionalInstId; 
	// 
	private Order refOrder;
		

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

	public String getOptionalInstId() {
		return optionalInstId;
	}

	public void setOptionalInstId(String optionalInstId) {
		this.optionalInstId = optionalInstId;
	}

	public Order getRefOrder() {
		return refOrder;
	}

	public void setRefOrder(Order refOrder) {
		this.refOrder = refOrder;
	}
}
