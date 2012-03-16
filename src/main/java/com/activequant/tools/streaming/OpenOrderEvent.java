package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;


public class OpenOrderEvent extends TradingDataEvent {
	
	private String side, type, orderId; 
	private Double price, quantity; 
	public OpenOrderEvent(String tradeableId, TimeStamp ts, String orderId, String side, String type, Double price, Double quantity) {
		super(ts,OpenOrderEvent.class.getCanonicalName(), tradeableId);
		this.side = side; 
		this.type = type; 
		this.price = price; 
		this.orderId = orderId; 
		this.quantity = quantity; 
	}
	
	public ETransportType getEventType(){return ETransportType.TRAD_DATA;}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

}
