package com.activequant.domainmodel.trade.order;

public class SingleLegOrder extends Order {

	private String tradInstId;
	private double quantity;

	private double openQuantity;
	private OrderSide orderSide;

	public OrderSide getOrderSide() {
		return orderSide;
	}

	public void setOrderSide(OrderSide orderSide) {
		this.orderSide = orderSide;
	}

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

	public double getOpenQuantity() {
		return openQuantity;
	}

	public void setOpenQuantity(double openQuantity) {
		this.openQuantity = openQuantity;
	}

}
