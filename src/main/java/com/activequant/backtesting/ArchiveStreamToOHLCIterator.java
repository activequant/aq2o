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
    private double open, high, low, close;
    private int resInSeconds; 
    private String OPEN="OPEN", HIGH = "HIGH", LOW="LOW", CLOSE = "CLOSE", VOL="VOLUME";
    private MultiValueTimeSeriesIterator streamIterator; 
     
     
    public ArchiveStreamToOHLCIterator(String mdiId, TimeFrame timeFrame, TimeStamp startTime, TimeStamp endTime, IArchiveReader archiveReader) throws Exception {
        this.mdiId = mdiId;
        this.tdiId = mdiId; 
        resInSeconds = timeFrame.getMinutes()*60;       
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
        if(valueMap.getB().containsKey(HIGH))open = valueMap.getB().get(HIGH);
        if(valueMap.getB().containsKey(LOW))open = valueMap.getB().get(LOW);
        if(valueMap.getB().containsKey(CLOSE))open = valueMap.getB().get(CLOSE);
        
        OHLCV o = new OHLCV();
        o.setMdiId(mdiId);
        o.setOpen(open);
        o.setHigh(high);
        o.setLow(low);
        o.setClose(close);
        o.setVolume(0.0);
        o.setResolutionInSeconds(resInSeconds);
        o.setTimeStamp(valueMap.getA());
        return o; 

    }
}
