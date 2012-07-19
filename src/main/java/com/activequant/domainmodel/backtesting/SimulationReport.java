package com.activequant.domainmodel.backtesting;

import java.io.Serializable;

/**
 * Container that contains backtested simulation results. 
 * It cannot be sent around over the wire by default. 
 * 
 * @author GhostRider
 *
 */
public class SimulationReport implements Serializable{

	private BacktestConfiguration btConfig;
	private TimeSetup timeSetup;
	private String simulationStatus; 

	public BacktestConfiguration getBtConfig() {
		return btConfig;
	}

	public void setBtConfig(BacktestConfiguration btConfig) {
		this.btConfig = btConfig;
	}

	public TimeSetup getTimeSetup() {
		return timeSetup;
	}

	public void setTimeSetup(TimeSetup timeSetup) {
		this.timeSetup = timeSetup;
	}

	public String getSimulationStatus() {
		return simulationStatus;
	}

	public void setSimulationStatus(String simulationStatus) {
		this.simulationStatus = simulationStatus;
	} 
	
}
