package com.activequant.backtesting.reporting;

import java.util.HashMap;
import java.util.Map;

import com.activequant.timeseries.TSContainer2;

/**
 * deprecated right from the start. Will become a persistent entity soon. 
 * 
 * @author GhostRider
 *
 */
public class BacktestStatistics {
	
	final private Map<String, Object> statistics = new HashMap<String, Object>();
	
	public void calcPNLStats(TSContainer2 cumPnlSeries){
		
	}

	public void calcPosStats(TSContainer2 positionSeries){
		
	}
	public void calcRiskStats(TSContainer2 valuatedPositionSeries){
		
	}
	
	public Map<String, Object> getStatistics() {
		return statistics;
	}
	
}
