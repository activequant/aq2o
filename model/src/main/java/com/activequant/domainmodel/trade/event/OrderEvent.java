package com.activequant.domainmodel.trade.event;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.trade.order.Order;

@Entity
@MappedSuperclass
public abstract class OrderEvent extends StreamEvent {

	public OrderEvent(String className) {
		super(className);
	}

	//
	@Column
	private String refOrderId;
	//
	@Column
	private String optionalInstId;
	//
	@Column
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
