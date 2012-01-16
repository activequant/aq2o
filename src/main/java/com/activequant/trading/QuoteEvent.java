package com.activequant.trading;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.tools.streaming.TimeStreamEvent;
import com.activequant.transport.ETransportType;

public class QuoteEvent extends TimeStreamEvent{
	
	public QuoteEvent(TimeStamp ts) {
		super(ts);
	}

	public ETransportType getEventType(){return ETransportType.MARKET_DATA;}
	
	private final String tradeableInstrumentId = "";
	private final Double price = 0.0; 
	private final Double quantity = 0.0; 
	private final OrderSide side = null; 
	
	public Double getPrice() {
		return price;
	}

	public Double getQuantity() {
		return quantity;
	}

	public String getTradeableInstrumentId() {
		return tradeableInstrumentId;
	}

	

}
