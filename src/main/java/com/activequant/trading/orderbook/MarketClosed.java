package com.activequant.trading.orderbook;

public class MarketClosed extends MarketState {

	@Override
	public String getType() {
		return "CLOSED";
	}

}
