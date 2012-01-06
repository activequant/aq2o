package com.activequant.domainmodel.trade.order;

public class LimitOrder extends SingleLegOrder {
	
	private double limitPrice;

	public double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(double limitPrice) {
		this.limitPrice = limitPrice;
	}

}
