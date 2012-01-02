package com.activequant.trading.virtual;

import com.activequant.trading.IOrderTracker;
import com.activequant.trading.StreamEvent;
import com.activequant.trading.TimeStreamEvent;

public class VirtualExchange {

	public IOrderTracker prepareOrder(){
		return null; 
	}
	
	public void processStreamEvent(StreamEvent streamEvent){
		if(streamEvent instanceof TimeStreamEvent){
			
		}
	}
	
	
}
