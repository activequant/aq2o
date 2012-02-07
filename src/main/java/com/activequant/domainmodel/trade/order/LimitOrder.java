package com.activequant.domainmodel.trade.order;

public class LimitOrder extends SingleLegOrder {
	
	private Double limitPrice;

	public Double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(Double limitPrice) {
		this.limitPrice = limitPrice;
	}

}
