package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;

public class DoubleValStreamEvent extends TimeStreamEvent {

	private final Double payload;

	public DoubleValStreamEvent(TimeStamp d8t6, Double payload) {
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
