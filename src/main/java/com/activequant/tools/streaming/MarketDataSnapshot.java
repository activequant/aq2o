package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;

public class MarketDataSnapshot extends MarketDataEvent {

	private double[] bidPrices, askPrices, bidSizes, askSizes;
	private String tradeableId;

	public MarketDataSnapshot(String mdiId, String tradeableId, TimeStamp ts, double[] bidPrices, double[] askPrices,
			double[] bidSizes, double[] askSizes) {
		super(ts, MarketDataSnapshot.class.getCanonicalName(), mdiId);
		this.tradeableId = tradeableId;
		this.bidPrices = bidPrices;
		this.askPrices = askPrices; 
		this.bidSizes = bidSizes; 
		this.askSizes = askSizes; 
	}

	public ETransportType getEventType() {
		return ETransportType.MARKET_DATA;
	}

	public String getTradeableInstrumentId() {
		return tradeableId;
	}

	public String getTradeableId() {
		return tradeableId;
	}

	public void setTradeableId(String tradeableId) {
		this.tradeableId = tradeableId;
	}

	public double[] getBidPrices() {
		return bidPrices;
	}

	public void setBidPrices(double[] bidPrices) {
		this.bidPrices = bidPrices;
	}

	public double[] getAskPrices() {
		return askPrices;
	}

	public void setAskPrices(double[] askPrices) {
		this.askPrices = askPrices;
	}

	public double[] getBidSizes() {
		return bidSizes;
	}

	public void setBidSizes(double[] bidSizes) {
		this.bidSizes = bidSizes;
	}

	public double[] getAskSizes() {
		return askSizes;
	}

	public void setAskSizes(double[] askSizes) {
		this.askSizes = askSizes;
	}

}
