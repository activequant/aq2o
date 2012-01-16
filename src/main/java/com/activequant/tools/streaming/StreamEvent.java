package com.activequant.tools.streaming;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;


public abstract class StreamEvent extends PersistentEntity {
	public StreamEvent(String className){
		super(className);
	}
	public abstract ETransportType getEventType();
	public abstract TimeStamp getTimeStamp();
}
