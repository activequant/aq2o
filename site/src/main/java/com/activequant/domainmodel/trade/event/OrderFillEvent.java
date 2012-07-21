package com.activequant.domainmodel.trade.event;


public class OrderFillEvent extends OrderEvent {
	private double fillAmount;
	private double fillPrice;
	private String side; 
	private double leftQuantity; 

	public OrderFillEvent(){
		super(OrderFillEvent.class.getCanonicalName());
	}
	
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
	

	public String toString(){
		return "Order " + super.getRefOrderId() + " filled: " + fillAmount +"@" + fillPrice; 
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public double getLeftQuantity() {
		return leftQuantity;
	}

	public void setLeftQuantity(double leftQuantity) {
		this.leftQuantity = leftQuantity;
	}
	
}
