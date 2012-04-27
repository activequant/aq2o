package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;

public class PositionEvent extends TradingDataEvent {

	private Double price, quantity;

	public PositionEvent(String tradeableId, TimeStamp ts, Double price, Double quantitye) {
		super(ts, PositionEvent.class.getCanonicalName(), tradeableId);
		this.price = price;
		this.quantity = quantitye;
	}

	public ETransportType getEventType() {
		return ETransportType.TRAD_DATA;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	public String toString(){
		return "PositionEvent. " + getTradInstId()+ ": " +  quantity + "@" + price; 
	}

}
