package com.activequant.trading;

import com.activequant.tools.streaming.StreamEvent;

public class ExecutionEvent extends StreamEvent{
	
	public String getEventType(){return "EXECUTION";}
	
	private final String tradeableInstrumentId = "";
	private final Double price = 0.0; 
	private final Double quantity = 0.0; 
	
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
