package com.activequant.domainmodel.streaming;

public interface IEventSink {

	void process(StreamEvent se);
	
}
