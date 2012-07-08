package com.activequant.backtesting;

import java.util.Map;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.tools.streaming.StreamEventIterator;
import com.activequant.tools.streaming.TimeStreamEvent;

/**
 * 
 * @author GhostRider
 *
 */
public class ArchiveStreamToOHLCIterator extends StreamEventIterator<TimeStreamEvent> {

    private String mdiId, tdiId; 
    private Double open, high, low, close, volume; 
    private int resInSeconds;
    private long offset; 
    private String OPEN="OPEN", HIGH = "HIGH", LOW="LOW", CLOSE = "CLOSE", VOL="VOLUME";
    private MultiValueTimeSeriesIterator streamIterator; 
     
     
    public ArchiveStreamToOHLCIterator(String mdiId, TimeFrame timeFrame, TimeStamp startTime, TimeStamp endTime, IArchiveReader archiveReader) throws Exception {
        this.mdiId = mdiId;
        this.tdiId = mdiId; 
        resInSeconds = timeFrame.getMinutes()*60;
        offset = resInSeconds * 1000l * 1000l * 1000l; 
        this.streamIterator = archiveReader.getMultiValueStream(mdiId, startTime, endTime);     
    }
    
    

    @Override
    public boolean hasNext() {
       return streamIterator.hasNext();
    }

    @Override
    public OHLCV next() {
        Tuple<TimeStamp, Map<String, Double>> valueMap = streamIterator.next();
        if(valueMap.getB().containsKey(OPEN))open = valueMap.getB().get(OPEN);
        if(valueMap.getB().containsKey(HIGH))high = valueMap.getB().get(HIGH);
        if(valueMap.getB().containsKey(LOW))low = valueMap.getB().get(LOW);
        if(valueMap.getB().containsKey(CLOSE))close = valueMap.getB().get(CLOSE);
        if(valueMap.getB().containsKey(VOL))volume = valueMap.getB().get(VOL);
        
        OHLCV o = new OHLCV();
        o.setMdiId(mdiId);
        o.setOpen(open);
        o.setHigh(high);
        o.setLow(low);
        o.setClose(close);
        o.setVolume(volume);
        o.setResolutionInSeconds(resInSeconds);
        // shift it, so that it looks as if it emitted at the end of a candle for replaying only
        // NOTE: In live streaming mode and for charting, it is proper to stamp a candle with the beginning timestamp!! 
        o.setTimeStamp(new TimeStamp(valueMap.getA().getNanoseconds() + offset));
        return o; 

    }



	public long getOffset() {
		return offset;
	}



	public void setOffset(long offset) {
		this.offset = offset;
	}
}
