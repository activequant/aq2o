package com.activequant.domainmodel.streaming;

import com.activequant.domainmodel.ETransportType;

public interface IEventSource {

	void subscribe(IEventSink sink, ETransportType eventType);
	void unsubscribe(IEventSink sink, ETransportType eventType);
	void start();
	
}
