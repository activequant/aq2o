package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;

public abstract class MarketDataEvent extends TimeStreamEvent {
	
	private String mdiId; 
	
	public ETransportType getEventType(){return ETransportType.MARKET_DATA;}
	
	public MarketDataEvent(TimeStamp ts, String className, String mdiId)
	{
		super(ts, className);
		this.mdiId = mdiId; 
	}
		
	@Override
	public String getId() {
		return getTimeStamp().toString();
	}


	public String getMdiId() {
		return mdiId;
	}

	public void setMdiId(String mdiId) {
		this.mdiId = mdiId;
	}

}
