package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;

public class MarketDataSnapshot extends MarketDataEvent {

	private final double[] bidPrices, askPrices, bidSizes, askSizes;
	private final String mdiId;

	public MarketDataSnapshot(String mdiId, TimeStamp ts, double[] bidPrices, double[] askPrices, double[] bidSizes,
			double[] askSizes) {
		super(ts, MarketDataSnapshot.class.getCanonicalName(), mdiId);
		this.bidPrices = bidPrices;
		this.askPrices = askPrices;
		this.bidSizes = bidSizes;
		this.askSizes = askSizes;
		this.mdiId = mdiId;
	}

	public ETransportType getEventType() {
		return ETransportType.MARKET_DATA;
	}

	public String getMarketDataId() {
		return mdiId;
	}

	public String getMdiId() {
		return mdiId;
	}

	public double[] getBidPrices() {
		return bidPrices;
	}

	public double[] getAskPrices() {
		return askPrices;
	}

	public double[] getBidSizes() {
		return bidSizes;
	}

	public double[] getAskSizes() {
		return askSizes;
	}

}
