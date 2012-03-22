package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;

public class InformationalEvent extends TimeStreamEvent {

	private final String text; 
	
	public InformationalEvent(TimeStamp ts, String text) {
		super(ts, InformationalEvent.class.getCanonicalName());
		this.text = text; 
	}

	public String getText() {
		return text;
	}

}
