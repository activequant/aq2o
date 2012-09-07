package com.activequant.domainmodel.streaming;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.annotations.Property;
import com.activequant.utils.ArrayUtils;

public class MarketDataSnapshot extends MarketDataEvent {

	private double[] bidPrices, askPrices, bidSizes, askSizes;
	private String mdiId;
	private String tdiId; 

	public MarketDataSnapshot(){
		super(MarketDataSnapshot.class.getCanonicalName());
	}
	
	public MarketDataSnapshot(String mdiId, TimeStamp ts, double[] bidPrices, double[] askPrices, double[] bidSizes,
			double[] askSizes) {
		super(ts, MarketDataSnapshot.class.getCanonicalName(), mdiId);
		this.bidPrices = bidPrices;
		this.askPrices = askPrices;
		this.bidSizes = bidSizes;
		this.askSizes = askSizes;
		this.mdiId = mdiId;
		this.tdiId = tdiId; 
	}

	public boolean liquid(){
		if(bidPrices.length>0 && askPrices.length>0){
			return true; 
		}
		return false; 
	}
	
	public String toString(){
		return mdiId + " - " + getTimeStamp().getDate() + " - " + ArrayUtils.toString(bidPrices) + " - " + ArrayUtils.toString(askPrices); 
	}
	
	public ETransportType getEventType() {
		return ETransportType.MARKET_DATA;
	}
	
	@Property
	public String getMdiId() {
		return mdiId;
	}
	@Property
	public double[] getBidPrices() {
		return bidPrices;
	}
	@Property
	public double[] getAskPrices() {
		return askPrices;
	}
	@Property
	public double[] getBidSizes() {
		return bidSizes;
	}
	@Property
	public double[] getAskSizes() {
		return askSizes;
	}

	public void setBidPrices(double[] bidPrices) {
		this.bidPrices = bidPrices;
	}

	public void setAskPrices(double[] askPrices) {
		this.askPrices = askPrices;
	}

	public void setBidSizes(double[] bidSizes) {
		this.bidSizes = bidSizes;
	}

	public void setAskSizes(double[] askSizes) {
		this.askSizes = askSizes;
	}

	public void setMdiId(String mdiId) {
		this.mdiId = mdiId;
	}

	@Property
	public String getTdiId() {
		return tdiId;
	}

	public void setTdiId(String tdiId) {
		this.tdiId = tdiId;
	}

}
