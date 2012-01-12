package com.activequant.trading;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.tools.streaming.TimeStreamEvent;

public class QuoteEvent extends TimeStreamEvent{
	
	public QuoteEvent(Date8Time6 ts) {
		super(ts);
	}

	public String getEventType(){return "QUOTE";}
	
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
