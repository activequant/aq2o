package com.activequant.trading;

import com.activequant.domainmodel.Date8Time6;

public class TimeStreamEvent extends StreamEvent {
	private Date8Time6 timeStamp;

	public Date8Time6 getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date8Time6 timeStamp) {
		this.timeStamp = timeStamp;
	} 
}
