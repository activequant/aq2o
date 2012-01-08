package com.activequant.tools.streaming;

public interface IEventSource {

	void subscribe(IEventSink sink, String eventType);
	void unsubscribe(IEventSink sink, String eventType);
	void start();
}
