package com.activequant.backtesting;

import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.timeseries.TSContainer2;

public interface IFeeCalculator {

	/**
	 * implement this method to plug in your own fee calculator.  
	 * 
	 * @param orderEvent
	 */
	void track(OrderEvent orderEvent);
	
	/**
	 * returns a matrix with fees as they occur. 
	 * 
	 * contract:
	 * one instrument per column 
	 * 
	 * @return the fees per instrument per time stamp
	 */
	TSContainer2 feesSeries();
}
