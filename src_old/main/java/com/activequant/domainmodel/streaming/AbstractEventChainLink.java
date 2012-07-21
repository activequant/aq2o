package com.activequant.domainmodel.streaming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.activequant.domainmodel.ETransportType;

public abstract class AbstractEventChainLink implements IEventSink, IEventSource {

	private Map<ETransportType, List<IEventSink>> subscriberMap = 
			new HashMap<ETransportType, List<IEventSink>>();

	
	public List<IEventSink> getSubscriberList(ETransportType type){
		if(!subscriberMap.containsKey(type)){
			subscriberMap.put(type, new ArrayList<IEventSink>());
		}
		return subscriberMap.get(type);
	}
	
	@Override
	public void subscribe(IEventSink sink,ETransportType eventType) {
		getSubscriberList(eventType).add(sink);
	}

	public void unsubscribe(IEventSink sink, ETransportType eventType) {
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
