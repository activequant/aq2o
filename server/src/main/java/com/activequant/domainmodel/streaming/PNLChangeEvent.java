package com.activequant.domainmodel.streaming;

import com.activequant.domainmodel.TimeStamp;

/**
 * 
 * @author GhostRider
 *
 */
public class PNLChangeEvent extends TradingDataEvent {

	private final Double change;
	
	public PNLChangeEvent(TimeStamp ts, String tradInstId, double value)
	{
		super(ts, PNLChangeEvent.class.getCanonicalName(), tradInstId);
		this.change = value; 
	}
	
	public Double getChange() {
		return change;
	}
	
}
