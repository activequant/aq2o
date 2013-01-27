package com.activequant.domainmodel.trade.event;

import com.activequant.utils.UniqueTimeStampGenerator;

public class OrderCancellationRejectedEvent extends OrderEvent {
	private String reason = "";

	public OrderCancellationRejectedEvent() {
		super(OrderCancellationRejectedEvent.class.getCanonicalName());
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}

	@Override
	public String getId() {
		return "OCRE." + nullSafe(getTimeStamp());
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String toString() {
		return "Order cancellation " + super.getRefOrderId() + " rejected: "
				+ reason;
	}
}
