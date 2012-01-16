package com.activequant.trading;

import com.activequant.tools.streaming.IEventSink;

public interface ITradingSystem extends IEventSink {

	/**
	 * sets the environment. 
	 * 
	 * @param env
	 */
	void environment(TradingSystemEnvironment env);
	
	void initialize();
	
	void start();

	void stop();
	
}
