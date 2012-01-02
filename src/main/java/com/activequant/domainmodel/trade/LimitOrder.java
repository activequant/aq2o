package com.activequant.domainmodel.trade;

public class LimitOrder extends SingleLegOrder {
	private double limitPrice;
	private double limitQuantity;

	public double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(double limitPrice) {
		this.limitPrice = limitPrice;
	}

	public double getLimitQuantity() {
		return limitQuantity;
	}

	public void setLimitQuantity(double limitQuantity) {
		this.limitQuantity = limitQuantity;
	}
}
