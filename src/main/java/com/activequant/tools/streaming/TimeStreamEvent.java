package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;

public class TimeStreamEvent extends StreamEvent {
	
	
	private final TimeStamp timeStamp;
	public String getEventType(){return "TIME";}

	public TimeStreamEvent(TimeStamp ts)
	{
		this.timeStamp = ts; 
	}
	
	public TimeStamp getTimeStamp() {
		return timeStamp;
	}
}
