package com.activequant.transport.memory;

import java.util.Map;

import com.activequant.transport.IPublisher;
import com.activequant.utils.events.Event;

public class InMemoryPublisher implements IPublisher {

	private Event<Map<String, Object>> event;
	
	InMemoryPublisher(Event<Map<String, Object>> event){
		this.event = event; 
	}
	
	@Override
	public void send(Map<String, Object> message) throws Exception {
		event.fire(message);
	}

}
