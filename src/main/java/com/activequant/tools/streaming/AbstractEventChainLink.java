package com.activequant.tools.streaming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractEventChainLink implements IEventSink, IEventSource {

	private Map<String, List<IEventSink>> subscriberMap = 
			new HashMap<String, List<IEventSink>>();

	
	public List<IEventSink> getSubscriberList(String type){
		if(!subscriberMap.containsKey(type)){
			subscriberMap.put(type, new ArrayList<IEventSink>());
		}
		return subscriberMap.get(type);
	}
	
	@Override
	public void subscribe(IEventSink sink,String eventType) {
		getSubscriberList(eventType).add(sink);
	}

	public void unsubscribe(IEventSink sink, String eventType) {
		getSubscriberList(eventType).remove(sink);
	}

	@Override
	public void process(StreamEvent se) {
		for(IEventSink sink : getSubscriberList(se.getEventType())){
			sink.process(se);
		}
	}
	
	public abstract void start();

}
