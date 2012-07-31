package com.activequant.interfaces.trading;

import com.activequant.domainmodel.AlgoConfig;
import com.activequant.interfaces.streaming.IStreamEventSink;
import com.activequant.trading.TradingSystemEnvironment;

/**
 * 
 * 
 * @author GhostRider
 *
 */
public interface ITradingSystem extends IStreamEventSink {

	/**
	 * Used to inject the environment.
	 *  
	 * @param env
	 */
	void environment(TradingSystemEnvironment env);
	
	/**
	 * Called after the environment has been injected. Now is the time
	 * to initialize, for example to subscribe to certain market data 
	 * streams, etc.    
	 * 
	 * @throws Exception
	 */
	void initialize() throws Exception;
	
	/**
	 * Called to signal that the trading system should start. 
	 * 
	 * @throws Exception
	 */
	void start() throws Exception;

	
	/**
	 * Called to signal that the trading system should stop. 
	 * It is up to the trading system to do what it has to do, such 
	 * as liquidating positions, etc. 
	 * 
	 * @throws Exception
	 */
	void stop() throws Exception;
	
	/**
	 * Queried from outside, to see whether the trading system is
	 * running or not. 
	 * 
	 * @return
	 */
	boolean isRunning(); 
	
	/**
	 * Must return the current algo configuration. 
	 * For example during a back test.
	 * 
	 * Feel free to derive your own class extension from AlgoConfig
	 * 
	 * @return
	 */
	public AlgoConfig getAlgoConfig();
	
}
