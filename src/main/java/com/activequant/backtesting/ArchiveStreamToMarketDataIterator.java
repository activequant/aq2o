package com.activequant.backtesting;

import java.util.Map;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.tools.streaming.MarketDataEvent;
import com.activequant.tools.streaming.MarketDataSnapshot;
import com.activequant.tools.streaming.StreamEventIterator;

public class ArchiveStreamToMarketDataIterator extends StreamEventIterator<MarketDataEvent> {

    private String mdiId, tdiId; 
    private MultiValueTimeSeriesIterator streamIterator; 
    private double[] bid, ask, bidQ, askQ; 
     
    public ArchiveStreamToMarketDataIterator(String mdiId, TimeStamp startTime, TimeStamp endTime, IArchiveReader archiveReader) throws Exception {
        this.mdiId = mdiId;
        this.tdiId = mdiId; 
        this.streamIterator = archiveReader.getMultiValueStream(mdiId, startTime, endTime);
        bid = new double[1];
        ask = new double[1];
        bidQ = new double[1];
        askQ = new double[1];          
    }
    
    public ArchiveStreamToMarketDataIterator(String mdiId, String tdiId, TimeStamp startTime, TimeStamp endTime, IArchiveReader archiveReader) throws Exception {
        this.mdiId = mdiId;
        this.streamIterator = archiveReader.getMultiValueStream(mdiId, startTime, endTime);
        bid = new double[1];
        ask = new double[1];
        bidQ = new double[1];
        askQ = new double[1];          
        this.tdiId = tdiId; 
    }
    
    

    @Override
    public boolean hasNext() {
       return streamIterator.hasNext();
    }

    @Override
    public MarketDataEvent next() {
        Tuple<TimeStamp, Map<String, Double>> valueMap = streamIterator.next();
        if(valueMap.getB().containsKey("BID"))bid[0] = valueMap.getB().get("BID");
        if(valueMap.getB().containsKey("ASK"))ask[0] = valueMap.getB().get("ASK");
        if(valueMap.getB().containsKey("BIDQUANTITY"))bidQ[0] = valueMap.getB().get("BIDQUANTITY");
        if(valueMap.getB().containsKey("ASKQUANTITY"))askQ[0] = valueMap.getB().get("ASKQUANTITY");
        

        MarketDataSnapshot mds = new MarketDataSnapshot();
        mds.setMdiId(this.mdiId);
        mds.setTdiId(this.tdiId);
        mds.setAskPrices(ask);
        mds.setBidPrices(bid);
        mds.setAskSizes(askQ);
        mds.setBidSizes(bidQ);
        mds.setTimeStamp(valueMap.getA());
        return mds; 

    }
}
