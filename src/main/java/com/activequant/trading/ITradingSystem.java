package com.activequant.trading;

import com.activequant.tools.streaming.IEventSink;

public interface ITradingSystem extends IEventSink {

	TradingSystemContainer container();
	
	void start();

	void stop();
	
}
