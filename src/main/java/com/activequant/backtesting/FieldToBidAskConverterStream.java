package com.activequant.backtesting;

import java.util.Map;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.tools.streaming.MarketDataEvent;
import com.activequant.tools.streaming.MarketDataSnapshot;
import com.activequant.tools.streaming.StreamEventIterator;

/**
 * 
 * converts a field to a stream. such as PX_SETTLE of daily data. 
 * 
 * @author ustaudinger
 *
 */
public class FieldToBidAskConverterStream extends StreamEventIterator<MarketDataEvent> {

    private String mdiId, tdiId, fieldName;  
    private TimeSeriesIterator streamIterator; 
    private double[] bid, ask, bidQ, askQ; 
    private double bidQuantity, askQuantity, bidOffset, askOffset;
     
    
    public FieldToBidAskConverterStream(String mdiId, String fieldName, TimeStamp startTime, TimeStamp endTime, IArchiveReader archiveReader) throws Exception {
        this.mdiId = mdiId;
        this.tdiId = mdiId; 
        this.fieldName = fieldName;
        this.streamIterator = archiveReader.getTimeSeriesStream(mdiId, fieldName, startTime, endTime);
        bid = new double[1];
        ask = new double[1];
        bidQ = new double[1];
        askQ = new double[1];          
    }
    
    public FieldToBidAskConverterStream(String mdiId, String tdiId, String fieldName, TimeStamp startTime, TimeStamp endTime, IArchiveReader archiveReader) throws Exception {
        this.mdiId = mdiId;
        this.fieldName = fieldName; 
        this.streamIterator = archiveReader.getTimeSeriesStream(mdiId, fieldName, startTime, endTime);
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
        Tuple<TimeStamp, Double> valueMap = streamIterator.next();
        
        // take the value and create a synthetic bid and ask out of  it. 
        
        ask[0] = valueMap.getB()+this.getAskOffset();
        bid[0] = valueMap.getB()+this.getBidOffset();
        askQ[0] = this.getAskQuantity();
        bidQ[0] = this.getBidQuantity();
        
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

	public double getBidQuantity() {
		return bidQuantity;
	}

	public void setBidQuantity(double bidQuantity) {
		this.bidQuantity = bidQuantity;
	}

	public double getAskQuantity() {
		return askQuantity;
	}

	public void setAskQuantity(double askQuantity) {
		this.askQuantity = askQuantity;
	}

	public double getBidOffset() {
		return bidOffset;
	}

	public void setBidOffset(double bidOffset) {
		this.bidOffset = bidOffset;
	}

	public double getAskOffset() {
		return askOffset;
	}

	public void setAskOffset(double askOffset) {
		this.askOffset = askOffset;
	}
}
