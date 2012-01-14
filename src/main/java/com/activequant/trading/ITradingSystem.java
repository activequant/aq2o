package com.activequant.trading;

import com.activequant.tools.streaming.IEventSink;

public interface ITradingSystem extends IEventSink {

	TradingSystemEnvironment container();
	
	void start();

	void stop();
	
}
