package com.activequant.domainmodel.trade.event;

import com.activequant.domainmodel.annotations.Property;
import com.activequant.utils.UniqueTimeStampGenerator;

public class OrderFillEvent extends OrderEvent {
	private double fillAmount;
	private double fillPrice;
	private String side;
	private double leftQuantity;
	private String execId;

	private int resend = 0;

	public OrderFillEvent() {
		super(OrderFillEvent.class.getCanonicalName());
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}

	@Override
	public String getId() {
		return "OFE." + nullSafe(getTimeStamp());
	}
	@Property
	public int getResend() {
		return resend;
	}

	public void setResend(int resend) {
		this.resend = resend;
	}
	@Property
	public String getExecId() {
		return execId;
	}

	public void setExecId(String execId) {
		this.execId = execId;
	}

	
	@Property
	public double getFillPrice() {
		return fillPrice;
	}

	public void setFillPrice(double fillPrice) {
		this.fillPrice = fillPrice;
	}
	@Property
	public double getFillAmount() {
		return fillAmount;
	}

	public void setFillAmount(double fillAmount) {
		this.fillAmount = fillAmount;
	}

	public String toString() {
		return "Order " + super.getRefOrderId() + " filled: " + fillAmount
				+ "@" + fillPrice;
	}
	@Property
	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}
	@Property
	public double getLeftQuantity() {
		return leftQuantity;
	}

	public void setLeftQuantity(double leftQuantity) {
		this.leftQuantity = leftQuantity;
	}

}
