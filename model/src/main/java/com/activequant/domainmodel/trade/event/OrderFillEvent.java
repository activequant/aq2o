package com.activequant.domainmodel.trade.event;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.utils.UniqueTimeStampGenerator;

@Entity
public class OrderFillEvent extends OrderEvent {
	@Column
	private double fillAmount;
	@Column
	private double fillPrice;
	@Column
	private OrderSide side;
	@Column
	private double leftQuantity;
	@Column
	private String execId;
	@Column
	private int resend = 0;

	public OrderFillEvent() {
		super(OrderFillEvent.class.getCanonicalName());
		setTimeStamp(UniqueTimeStampGenerator.getInstance().now());
	}

	@Override
	public String getId() {
		return "OFE." + nullSafe(getTimeStamp());
	}

	public int getResend() {
		return resend;
	}

	public void setResend(int resend) {
		this.resend = resend;
	}

	public String getExecId() {
		return execId;
	}

	public void setExecId(String execId) {
		this.execId = execId;
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

	public String toString() {
		return "Order " + super.getRefOrderId() + " filled: " + fillAmount
				+ "@" + fillPrice;
	}

	public OrderSide getSide() {
		return side;
	}

	public void setSide(OrderSide side) {
		this.side = side;
	}

	public double getLeftQuantity() {
		return leftQuantity;
	}

	public void setLeftQuantity(double leftQuantity) {
		this.leftQuantity = leftQuantity;
	}

}
