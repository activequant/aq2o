package com.activequant.domainmodel.trade.event;


public class OrderFillEvent extends OrderEvent {
	private double fillAmount;
	private double fillPrice; 

	public double getFillPrice() {
		return fillPrice;
	}

	public void setFillPrice(double fillPrice) {
		this.fillPrice = fillPrice;
	}

	public double getFillAmount() {
		return fillAmount;
	}

	public void setFillAmount(double fillAmount) {
		this.fillAmount = fillAmount;
	} 
	
}
