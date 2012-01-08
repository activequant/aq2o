package com.activequant.tools.streaming;

public interface IEventSink {

	void process(StreamEvent se);
	
}
