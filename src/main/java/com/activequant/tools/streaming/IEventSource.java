package com.activequant.tools.streaming;

import com.activequant.transport.ETransportType;

public interface IEventSource {

	void subscribe(IEventSink sink, ETransportType eventType);
	void unsubscribe(IEventSink sink, ETransportType eventType);
	void start();
}
