package com.activequant.backtesting;

import java.util.Map;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.tools.streaming.BBOEvent;
import com.activequant.tools.streaming.MarketDataEvent;
import com.activequant.tools.streaming.StreamEventIterator;

public class ArchiveStreamToMarketDataIterator extends StreamEventIterator<MarketDataEvent> {

    private String mdId;
    private MultiValueTimeSeriesIterator streamIterator; 
    private Double bid, ask, bidQ, askQ; 
    
    public ArchiveStreamToMarketDataIterator(String mdiId, TimeStamp startTime, TimeStamp endTime, IArchiveReader archiveReader) throws Exception {
        this.mdId = mdiId;
        this.streamIterator = archiveReader.getMultiValueStream(mdiId, startTime, endTime);

    }

    @Override
    public boolean hasNext() {
       return streamIterator.hasNext();
    }

    @Override
    public MarketDataEvent next() {
        Tuple<TimeStamp, Map<String, Double>> valueMap = streamIterator.next();
        if(valueMap.getB().containsKey("BID"))bid = valueMap.getB().get("BID");
        if(valueMap.getB().containsKey("ASK"))ask = valueMap.getB().get("ASK");
        if(valueMap.getB().containsKey("BIDQUANTITY"))bidQ = valueMap.getB().get("BIDQUANTITY");
        if(valueMap.getB().containsKey("ASKQUANTITY"))askQ= valueMap.getB().get("ASKQUANTITY");
        
        
        return new BBOEvent(this.mdId, this.mdId, valueMap.getA(), bid, bidQ, ask, askQ);

    }
}
