package com.activequant.tools.streaming;

import com.activequant.domainmodel.Date8Time6;

public class DoubleValStreamEvent extends TimeStreamEvent {

	private final Double payload;

	public DoubleValStreamEvent(Date8Time6 d8t6, Double payload) {
		super(d8t6);
		this.payload = payload;
	}

	@Override
	public String getEventType() {
		return "UNKNOWN";
	}

	public Double getPayload() {
		return payload;
	}

}
