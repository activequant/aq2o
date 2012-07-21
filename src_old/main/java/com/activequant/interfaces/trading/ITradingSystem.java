package com.activequant.interfaces.trading;

import com.activequant.domainmodel.AlgoConfig;
import com.activequant.domainmodel.streaming.IEventSink;
import com.activequant.trading.TradingSystemEnvironment;

public interface ITradingSystem extends IEventSink {

	/**
	 * used to inject the environment. 
	 * @param env
	 */
	void environment(TradingSystemEnvironment env);
	
	void initialize() throws Exception;
	
	void start() throws Exception;

	void stop() throws Exception;
	
	/**
	 * Must return the current algo configuration. For example during a back test. 
	 * 
	 * @return
	 */
	public AlgoConfig getAlgoConfig();
	
}
