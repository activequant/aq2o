package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;


public abstract class StreamEvent {
	public abstract String getEventType();
	public abstract TimeStamp getTimeStamp();
}
