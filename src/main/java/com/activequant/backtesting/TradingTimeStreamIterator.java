package com.activequant.backtesting;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.tools.streaming.StreamEventIterator;
import com.activequant.tools.streaming.TimeStreamEvent;

public class TradingTimeStreamIterator extends StreamEventIterator<TimeStreamEvent> {

	// time in nanoseconds. 
	private long endTime, currentTime;
	private final long step; 

	public TradingTimeStreamIterator(TimeStamp startTime, TimeStamp endTime, long stepWidthInNanoS){
		step = stepWidthInNanoS;
		this.currentTime = startTime.getNanoseconds() - step;  
		this.endTime = endTime.getNanoseconds();
	}
	
	@Override
	public boolean hasNext() {
		return currentTime < endTime;
	}

	@Override
	public TimeStreamEvent next() {
		currentTime = currentTime + step;
		
		TimeStreamEvent event = new TimeStreamEvent(new TimeStamp(currentTime));
		// 
		return event;
	}

}
