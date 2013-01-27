package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.annotations.Property;
import com.activequant.utils.UniqueTimeStampGenerator;

public class OrderRejectedEvent extends OrderEvent {
	private String reason = "";

	public OrderRejectedEvent() {
		super(OrderRejectedEvent.class.getCanonicalName());
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}

	@Override
	public String getId() {
		return "ORE." + nullSafe(getTimeStamp());
	}

	@Property
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String toString() {
		return "Order " + super.getRefOrderId() + " rejected: " + reason;
	}
}
