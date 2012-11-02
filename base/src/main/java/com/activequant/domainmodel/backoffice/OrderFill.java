package com.activequant.domainmodel.backoffice;

import net.sf.oval.constraint.NotNull;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.annotations.Property;

public class OrderFill extends PersistentEntity {

	@NotNull
	private String tradeableId;
	@NotNull
	private Double price;
	@NotNull
	private Long quantity;
	@NotNull
	private String brokerId;
	@NotNull
	private String providerId;
	@NotNull
	private Long timeStampInNanos;
	@NotNull
	private String orderSide;
	@NotNull
	private String providerAccountId;
	@NotNull
	private String brokerAccountId;
	@NotNull
	private String orderFillId;
	@NotNull
	private String orderId;

	@NotNull
	private String routeId;

	public OrderFill() {
		super(OrderFill.class.getCanonicalName());

	}
	@Override
	public String getId() {
		return "FILL." + nullSafe(tradeableId) + "." + nullSafe(brokerId) + "." + nullSafe(providerId) + "."
				+ nullSafe(orderFillId) + "." + nullSafe(orderId) + "." + nullSafe(routeId);
	}

	@Property
	public String getTradeableId() {
		return tradeableId;
	}

	public void setTradeableId(String tradeableId) {
		this.tradeableId = tradeableId;
	}

	@Property
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Property
	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Property
	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	@Property
	public Long getTimeStampInNanos() {
		return timeStampInNanos;
	}

	public void setTimeStampInNanos(Long timeStampInNanos) {
		this.timeStampInNanos = timeStampInNanos;
	}

	@Property
	public String getOrderSide() {
		return orderSide;
	}

	public void setOrderSide(String orderSide) {
		this.orderSide = orderSide;
	}

	@Property
	public String getOrderFillId() {
		return orderFillId;
	}

	public void setOrderFillId(String orderFillId) {
		this.orderFillId = orderFillId;
	}

	@Property
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Property
	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	@Property
	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	@Property
	public String getProviderAccountId() {
		return providerAccountId;
	}

	public void setProviderAccountId(String providerAccountId) {
		this.providerAccountId = providerAccountId;
	}

	@Property
	public String getBrokerAccountId() {
		return brokerAccountId;
	}

	public void setBrokerAccountId(String brokerAccountId) {
		this.brokerAccountId = brokerAccountId;
	}
}
