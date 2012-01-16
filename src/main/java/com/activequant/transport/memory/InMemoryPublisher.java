package com.activequant.transport.memory;

import java.util.Map;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.transport.IPublisher;
import com.activequant.utils.events.Event;

public class InMemoryPublisher implements IPublisher {

	private Event<Map<String, Object>> rawEvent;
	private Event<PersistentEntity> event;
	
	InMemoryPublisher(Event<Map<String, Object>> rawEvent, Event<PersistentEntity> event){
		this.rawEvent = rawEvent; 
		this.event = event; 
	}
	
	@Override
	public void send(Map<String, Object> message) throws Exception {
		rawEvent.fire(message);
	}

	@Override
	public void send(PersistentEntity entity) throws Exception {
		event.fire(entity);
	}

}
