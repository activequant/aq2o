package com.activequant.domainmodel.streaming;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeStamp;


public abstract class StreamEvent extends PersistentEntity {
	private TimeStamp timeStamp;
	public StreamEvent(String className){
		super(className);
	}
	public abstract ETransportType getEventType();
	public TimeStamp getTimeStamp(){
		return timeStamp; 
	}
	public void setTimeStamp(TimeStamp ts){
		timeStamp = ts; 
	}
}
