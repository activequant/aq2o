package com.activequant.interfaces.trading;

/**
 * 
 * @author GhostRider
 *
 */
public interface ITradingSystemFactory {

	/**
	 * You can shield several trading systems behind one proxy trading system. 
	 * @return
	 */
	public ITradingSystem getTradingSystem();
}
