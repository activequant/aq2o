package com.activequant.domainmodel.streaming;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeStamp;


public abstract class StreamEvent extends PersistentEntity {
	public StreamEvent(String className){
		super(className);
	}
	public abstract ETransportType getEventType();
	public abstract TimeStamp getTimeStamp();
}
