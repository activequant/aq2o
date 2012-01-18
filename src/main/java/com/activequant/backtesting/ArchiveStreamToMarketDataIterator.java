package com.activequant.backtesting;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.tools.streaming.BBOEvent;
import com.activequant.tools.streaming.MarketDataEvent;
import com.activequant.tools.streaming.StreamEventIterator;

public class ArchiveStreamToMarketDataIterator extends StreamEventIterator<MarketDataEvent> {

	// time in nanoseconds.
	private long endTime, startTime;
	private String mdId; 
	private TimeSeriesIterator tsi; 

	public ArchiveStreamToMarketDataIterator(String mdiId, String key, TimeStamp startTime, TimeStamp endTime,
			IArchiveReader archiveReader) throws Exception {
		this.mdId = mdiId;
		tsi = archiveReader.getTimeSeriesStream(mdiId, key, startTime, endTime);
		
	}

	@Override
	public boolean hasNext() {
		return tsi.hasNext();
	}

	@Override
	public MarketDataEvent next() {

		Tuple<TimeStamp, Double> tuple = tsi.next();
		/*
		BBOEvent bbo = new BBOEvent(mdId, tradId, new TimeStamp(currentTime),
				Math.random(), Math.random(), Math.random(), Math.random());
		//
		return bbo;
		*/
		return null; 
	}
}
