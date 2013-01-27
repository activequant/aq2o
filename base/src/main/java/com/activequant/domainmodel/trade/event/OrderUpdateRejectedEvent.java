package com.activequant.domainmodel.trade.event;

import com.activequant.utils.UniqueTimeStampGenerator;

public class OrderUpdateRejectedEvent extends OrderEvent {
	private String reason = "";

	public OrderUpdateRejectedEvent() {
		super(OrderUpdateRejectedEvent.class.getCanonicalName());
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}

	@Override
	public String getId() {
		return "OURE." + nullSafe(getTimeStamp());
	}

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
