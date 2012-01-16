package com.activequant.backtesting;

import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;

public class TradingTimeStream extends TimeSeriesIterator{

	// time in nanoseconds. 
	private long startTime, endTime, currentTime;
	Tuple<TimeStamp, Double> next = new Tuple<TimeStamp, Double>();
	private final long oneSecond = 1000 * 1000 * 1000; 

	public TradingTimeStream(TimeStamp startTime, TimeStamp endTime){
		this.startTime = startTime.getNanoseconds();
		this.currentTime = startTime.getNanoseconds() - oneSecond;  
		this.endTime = endTime.getNanoseconds();
	}
	
	@Override
	public boolean hasNext() {
		return startTime < endTime;
	}

	@Override
	public Tuple<TimeStamp, Double> next() {
		currentTime = currentTime + oneSecond;
		
		next.setA(new TimeStamp(currentTime));
		next.setB((double)currentTime);
		
		// 
		return next;
	}

}
