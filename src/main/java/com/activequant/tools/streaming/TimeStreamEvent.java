package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;

public class TimeStreamEvent extends StreamEvent {
	
	private final TimeStamp timeStamp;
	public ETransportType getEventType(){return ETransportType.TIME;}

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
	
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}

	@Override
	public String getId() {
		return timeStamp.toString();
	}
}
