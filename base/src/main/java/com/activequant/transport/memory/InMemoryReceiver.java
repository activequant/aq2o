package com.activequant.transport.memory;

import java.util.Map;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.utils.IEventSource;
import com.activequant.utils.events.Event;

public class InMemoryReceiver implements IReceiver {

	private Event<Map<String, Object>> rawMsgEvent;
	private Event<byte[]> rawEvent;
	private Event<PersistentEntity> event;
	
	InMemoryReceiver(Event<Map<String, Object>> rawEvent, Event<byte[]> raw, Event<PersistentEntity> event){
		this.rawMsgEvent = rawEvent;
		this.event = event; 
		this.rawEvent = raw; 
	}
	
	@Override
	public IEventSource<Map<String, Object>> getRawMsgRecEvent() {
		return rawMsgEvent;
	}
	
	@Override
	public IEventSource<PersistentEntity> getMsgRecEvent() {
		return event;
	}

	public IEventSource<byte[]> getRawEvent(){
		return rawEvent;
	}

}

