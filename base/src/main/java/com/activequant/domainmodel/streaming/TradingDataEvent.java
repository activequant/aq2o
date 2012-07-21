package com.activequant.domainmodel.streaming;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;

public abstract class TradingDataEvent extends TimeStreamEvent {
	
	private final String tradInstId; 
	
	public ETransportType getEventType(){return ETransportType.TRAD_DATA;}
	
	public TradingDataEvent(TimeStamp ts, String className, String instr)
	{
		super(ts, className);
		this.tradInstId = instr; 
	}
		
	@Override
	public String getId() {
		return getTimeStamp().toString();
	}

	public String getTradInstId() {
		return tradInstId;
	}

}
