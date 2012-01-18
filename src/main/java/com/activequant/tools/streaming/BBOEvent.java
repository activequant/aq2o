package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;


public class BBOEvent extends MarketDataEvent {
	
	private double bid, ask, bidQuantity, askQuantity;  
	private String tradeableId;

	public BBOEvent(String mdiId, String tradeableId, TimeStamp ts, 
			double bid, double bidQuantity, double ask, double askQuantity) {
		super(ts,BBOEvent.class.getCanonicalName(), mdiId);
		this.tradeableId = tradeableId; 
		this.ask = ask; 
		this.bid = bid; 
		this.askQuantity = askQuantity;
		this.bidQuantity = bidQuantity; 
	}

	public ETransportType getEventType(){return ETransportType.MARKET_DATA;}
	
	public String getTradeableInstrumentId(){return tradeableId;}

	public double getBid() {
		return bid;
	}

	public void setBid(double bid) {
		this.bid = bid;
	}

	public double getAsk() {
		return ask;
	}

	public void setAsk(double ask) {
		this.ask = ask;
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

	public String getTradeableId() {
		return tradeableId;
	}

	public void setTradeableId(String tradeableId) {
		this.tradeableId = tradeableId;
	}

	

}
