package com.activequant.domainmodel.streaming;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;

public abstract class MarketDataEvent extends TimeStreamEvent {
	
	private String mdiId; 
	private boolean resend = false; 
	
	public boolean isResend() {
		return resend;
	}

	public void setResend(boolean resend) {
		this.resend = resend;
	}

	public ETransportType getEventType(){return ETransportType.MARKET_DATA;}
	
	public MarketDataEvent(String className){
		super(className);
	}
	
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
