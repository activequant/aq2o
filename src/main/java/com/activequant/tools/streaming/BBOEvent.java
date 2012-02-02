package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;


public class BBOEvent extends MarketDataEvent {
	
	private Double bid, ask, bidQuantity, askQuantity;  
	private String tradeableId;

	public BBOEvent(String mdiId, String tradeableId, TimeStamp ts, 
			Double bid, Double bidQuantity, Double ask, Double askQuantity) {
		super(ts,BBOEvent.class.getCanonicalName(), mdiId);
		this.tradeableId = tradeableId; 
		this.ask = ask; 
		this.bid = bid; 
		this.askQuantity = askQuantity;
		this.bidQuantity = bidQuantity; 
	}

	public ETransportType getEventType(){return ETransportType.MARKET_DATA;}
	
	public String getTradeableInstrumentId(){return tradeableId;}

	public Double getBid() {
		return bid;
	}

	public void setBid(Double bid) {
		this.bid = bid;
	}

	public Double getAsk() {
		return ask;
	}

	public void setAsk(Double ask) {
		this.ask = ask;
	}

	public Double getBidQuantity() {
		return bidQuantity;
	}

	public void setBidQuantity(Double bidQuantity) {
		this.bidQuantity = bidQuantity;
	}

	public Double getAskQuantity() {
		return askQuantity;
	}

	public void setAskQuantity(Double askQuantity) {
		this.askQuantity = askQuantity;
	}

	public String getTradeableId() {
		return tradeableId;
	}

	public void setTradeableId(String tradeableId) {
		this.tradeableId = tradeableId;
	}

	

}
