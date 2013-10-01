package com.activequant.interfaces.backtesting;

import com.activequant.domainmodel.AlgoConfig;
import com.activequant.domainmodel.backtesting.BacktestConfiguration;
import com.activequant.domainmodel.backtesting.SimulationReport;

/**
 * interface defining the report generator. 
 * 
 * @author GhostRider
 *
 */
public interface IReportRenderer {
	void genReport(BacktestConfiguration btConfig, AlgoConfig[] algoConfigs, SimulationReport simReport);
		
}
