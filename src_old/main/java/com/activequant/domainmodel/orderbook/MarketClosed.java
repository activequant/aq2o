package com.activequant.domainmodel.orderbook;

public class MarketClosed extends MarketState {

	@Override
	public String getType() {
		return "CLOSED";
	}

}
