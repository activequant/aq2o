package com.activequant.trading.orderbook;

public class OrderBookUpdated extends OrderBookChange {

	@Override
	public ChangeTypeEnum getChangeType() {
		return ChangeTypeEnum.UPDATED;
	}

}
