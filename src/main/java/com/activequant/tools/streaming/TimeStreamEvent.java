package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;
import com.activequant.utils.annotations.Property;

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
