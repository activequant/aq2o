package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;

public class Tick extends MarketDataEvent {

	private final double price, quantity;
	private final int tickDirection;
	private final String mdiId;

	public Tick(String mdiId, TimeStamp ts, double price, double quantity, int tickDirection) {
		super(ts, Tick.class.getCanonicalName(), mdiId);		
		this.mdiId = mdiId;
		this.price = price; 
		this.quantity = quantity; 
		this.tickDirection = tickDirection; 
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

	public double getPrice() {
		return price;
	}

	public double getQuantity() {
		return quantity;
	}

	public int getTickDirection() {
		return tickDirection;
	}


}
