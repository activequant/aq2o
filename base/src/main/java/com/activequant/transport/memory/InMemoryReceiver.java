package com.activequant.transport.memory;

import java.util.Map;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.utils.IEventSource;
import com.activequant.utils.events.Event;

public class InMemoryReceiver implements IReceiver {

	private Event<Map<String, Object>> rawEvent;
	private Event<PersistentEntity> event;
	
	InMemoryReceiver(Event<Map<String, Object>> rawEvent, Event<PersistentEntity> event){
		this.rawEvent = rawEvent;
		this.event = event; 
	}
	
	@Override
	public IEventSource<Map<String, Object>> getRawMsgRecEvent() {
		return rawEvent;
	}
	
	@Override
	public IEventSource<PersistentEntity> getMsgRecEvent() {
		return event;
	}

	public IEventSource<byte[]> getRawEvent(){
		throw new RuntimeException("Not implemented.");
	}

}

