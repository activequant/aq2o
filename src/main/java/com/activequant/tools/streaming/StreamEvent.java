package com.activequant.tools.streaming;

import com.activequant.domainmodel.Date8Time6;


public abstract class StreamEvent {
	public abstract String getEventType();
	public abstract Date8Time6 getTimeStamp();
}
