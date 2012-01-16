package com.activequant.tools.streaming;

import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;

public abstract class MarketDataEvent extends TimeStreamEvent {
	
	private final MarketDataInstrument mdi; 
	
	public ETransportType getEventType(){return ETransportType.MARKET_DATA;}
	
	public MarketDataEvent(TimeStamp ts, String className, MarketDataInstrument mdi)
	{
		super(ts, className);
		this.mdi = mdi; 
	}
		
	@Override
	public String getId() {
		return getTimeStamp().toString();
	}


	public MarketDataInstrument getMdi() {
		return mdi;
	}

}
