package com.activequant.domainmodel.trade.order;

public class SingleLegOrder {

	private String tradInstId;
	private double quantity;

	public String getTradInstId() {
		return tradInstId;
	}

	public void setTradInstId(String tradInstId) {
		this.tradInstId = tradInstId;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
}
