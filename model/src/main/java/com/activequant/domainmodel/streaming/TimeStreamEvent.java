package com.activequant.domainmodel.streaming;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.annotations.Property;

public class TimeStreamEvent extends StreamEvent {
	
	private TimeStamp timeStamp;
	public ETransportType getEventType(){return ETransportType.TIME;}

	public TimeStreamEvent(String clazz){
		super(clazz);
	}
	
	public TimeStreamEvent(TimeStamp ts)
	{
		super(TimeStreamEvent.class.getCanonicalName());
		this.timeStamp = ts;
	}
	
	public TimeStreamEvent(TimeStamp ts, String className)
	{
		super(className);
		this.timeStamp = ts; 		
	}
	
	@Property
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}

	@Override
	public String getId() {
		return timeStamp.toString();
	}

	public void setTimeStamp(TimeStamp timeStamp) {
		this.timeStamp = timeStamp;
	}
}
