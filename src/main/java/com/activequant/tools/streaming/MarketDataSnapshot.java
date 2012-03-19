package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;
import com.activequant.utils.ArrayUtils;
import com.activequant.utils.annotations.Property;

public class MarketDataSnapshot extends MarketDataEvent {

	private double[] bidPrices, askPrices, bidSizes, askSizes;
	private String mdiId;

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
	}

	public String toString(){
		return mdiId + " - " + getTimeStamp().getDate() + " - " + ArrayUtils.toString(bidPrices) + " - " + ArrayUtils.toString(askPrices); 
	}
	
	public ETransportType getEventType() {
		return ETransportType.MARKET_DATA;
	}
	@Property
	public String getMarketDataId() {
		return mdiId;
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

}
