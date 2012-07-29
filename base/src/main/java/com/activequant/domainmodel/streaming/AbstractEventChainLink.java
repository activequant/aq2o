package com.activequant.domainmodel.streaming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.activequant.domainmodel.ETransportType;
import com.activequant.interfaces.streaming.IStreamEventSink;
import com.activequant.interfaces.streaming.IStreamEventSource;

public abstract class AbstractEventChainLink implements IStreamEventSink, IStreamEventSource {

	private Map<ETransportType, List<IStreamEventSink>> subscriberMap = 
			new HashMap<ETransportType, List<IStreamEventSink>>();

	
	public List<IStreamEventSink> getSubscriberList(ETransportType type){
		if(!subscriberMap.containsKey(type)){
			subscriberMap.put(type, new ArrayList<IStreamEventSink>());
		}
		return subscriberMap.get(type);
	}
	
	@Override
	public void subscribe(IStreamEventSink sink,ETransportType eventType) {
		getSubscriberList(eventType).add(sink);
	}

	public void unsubscribe(IStreamEventSink sink, ETransportType eventType) {
		getSubscriberList(eventType).remove(sink);
	}

	@Override
	public void process(StreamEvent se) {
		for(IStreamEventSink sink : getSubscriberList(se.getEventType())){
			sink.process(se);
		}
	}
	
	public abstract void start();

}
