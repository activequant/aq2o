package com.activequant.domainmodel.orderbook;

public class OrderBookUpdated extends OrderBookChange {

	@Override
	public ChangeTypeEnum getChangeType() {
		return ChangeTypeEnum.UPDATED;
	}

}
