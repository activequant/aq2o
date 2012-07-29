package com.activequant.domainmodel.streaming;

import com.activequant.domainmodel.TimeStamp;

/**
 * 
 * @author GhostRider
 *
 */
public class PNLChangeEvent extends TradingDataEvent {

	private final double change;
	private final double totalPnl; 
	
	public double getTotalPnl() {
		return totalPnl;
	}

	public PNLChangeEvent(TimeStamp ts, String tradInstId, double value, double totalPnl)
	{
		super(ts, PNLChangeEvent.class.getCanonicalName(), tradInstId);
		this.change = value;
		this.totalPnl = totalPnl;
	}
	
	public Double getChange() {
		return change;
	}
	
}
