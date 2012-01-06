package com.activequant.trading.virtual;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.trading.IOrderTracker;
import com.activequant.trading.StreamEvent;
import com.activequant.trading.TimeStreamEvent;

public class VirtualExchange {

	private Date8Time6 currentExchangeTime; 
	
	public Date8Time6 currentExchangeTime(){
		return currentExchangeTime; 
	}
	
	public IOrderTracker prepareOrder(){
		return null; 
	}
	
	public void processStreamEvent(StreamEvent streamEvent){
		if(streamEvent instanceof TimeStreamEvent){
			currentExchangeTime = ((TimeStreamEvent)streamEvent).getTimeStamp();
		}
	}
	
	public VirtualExchange(MarketDataInstrument[] instruments, int timeStreamGranularityInMs){
		
	}
	
	
	
}
