package com.activequant.backtesting.reporting;

import java.util.HashMap;
import java.util.Map;

import com.activequant.timeseries.TSContainer2;

public class BacktestStatistics {
	
	final private Map<String, Object> statistics = new HashMap<String, Object>();
	
	public void calculateStatistics(TSContainer2 cumPnlSeries){
		
	}

	public Map<String, Object> getStatistics() {
		return statistics;
	}
	
}
