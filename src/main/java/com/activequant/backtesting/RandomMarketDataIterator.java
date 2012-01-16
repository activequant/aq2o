package com.activequant.backtesting;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.tools.streaming.BBOEvent;
import com.activequant.tools.streaming.MarketDataEvent;
import com.activequant.tools.streaming.StreamEventIterator;

public class RandomMarketDataIterator extends StreamEventIterator<MarketDataEvent> {

	// time in nanoseconds.
	private long endTime, currentTime;
	private final long step;
	private String tradId, mdId; 

	public RandomMarketDataIterator(String tradId, String mdId, TimeStamp startTime, TimeStamp endTime,
			long stepWidthInNanoS) {
		step = stepWidthInNanoS;
		this.currentTime = startTime.getNanoseconds() - step;
		this.endTime = endTime.getNanoseconds();
	}

	@Override
	public boolean hasNext() {
		return currentTime < endTime;
	}

	@Override
	public MarketDataEvent next() {
		currentTime = currentTime + step;

		BBOEvent bbo = new BBOEvent(mdId, tradId, new TimeStamp(currentTime),
				Math.random(), Math.random(), Math.random(), Math.random());
		//
		return bbo;
	}
}
