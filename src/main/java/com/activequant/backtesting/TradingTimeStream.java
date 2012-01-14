package com.activequant.backtesting;

import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.Tuple;

public class TradingTimeStream extends TimeSeriesIterator{

	// time in nanoseconds. 
	private long startTime, endTime, currentTime;
	Tuple<Date8Time6, Double> next = new Tuple<Date8Time6, Double>();
	private final long oneSecond = 1000 * 1000 * 1000; 
	
	public TradingTimeStream(long startTime, long endTime){
		this.startTime = startTime;
		this.currentTime = startTime - oneSecond;  
		this.endTime = endTime; 
	}
	
	@Override
	public boolean hasNext() {
		return startTime < endTime;
	}

	@Override
	public Tuple<Date8Time6, Double> next() {
		currentTime = currentTime + oneSecond;
		
		next.setA(Date8Time6.now());
		next.setB((double)currentTime);
		
		// 
		return next;
	}

}
