package com.activequant.transport.memory;

import java.util.Map;

import com.activequant.transport.IReceiver;
import com.activequant.utils.events.Event;
import com.activequant.utils.events.IEventSource;

public class InMemoryReceiver implements IReceiver {

	private Event<Map<String, Object>> event;
	
	InMemoryReceiver(Event<Map<String, Object>> event){
		this.event = event; 
	}
	
	@Override
	public IEventSource<Map<String, Object>> getMsgRecEvent() {
		return event;
	}
}
