package com.activequant.tools.streaming;

import com.activequant.domainmodel.Date8Time6;

public class TimeStreamEvent extends StreamEvent {
	
	
	private final Date8Time6 timeStamp;
	public String getEventType(){return "TIME";}

	public TimeStreamEvent(Date8Time6 ts)
	{
		this.timeStamp = ts; 
	}
	
	public Date8Time6 getTimeStamp() {
		return timeStamp;
	}
}
