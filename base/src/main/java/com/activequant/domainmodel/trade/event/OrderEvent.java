package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.trade.order.Order;

public abstract class OrderEvent extends StreamEvent {

	public OrderEvent(String className) {
		super(className);
	}

	// 
	private String refOrderId;
	// 
	private String optionalInstId;
	//
	protected Order refOrder;

	// 
	@Override
	public ETransportType getEventType() {
		return ETransportType.TRAD_DATA;
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
