package com.activequant.domainmodel.backoffice;

import net.sf.oval.constraint.NotNull;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.annotations.Property;

public class ClearedTrade extends PersistentEntity {
	@NotNull
	private String tradeableId;
	@NotNull
	private Double price;
	@NotNull
	private Long quantity;
	@NotNull
	private Long timeStampInNanos;
	@NotNull
	private String orderSide;
	@NotNull
	private String clearingAccountId;
	@NotNull
	private String status;
	@NotNull
	private String clearedTradeId;
	@NotNull
	private String clearingFeeCurrency;
	@NotNull
	private String exchangeFeeCurrency;
	@NotNull
	private String brokerFeeCurrency;
	@NotNull
	private Double clearingFee;
	@NotNull
	private Double exchangeFee;
	@NotNull
	private Double brokerFee;
	@NotNull
	private String cusip;
	@NotNull
	private String uniqueId;

	private String subAccountId;
	private Long date8;

	public ClearedTrade() {
		super(ClearedTrade.class.getCanonicalName());
	}

	@Override
	public String getId() {
		return "CT." + nullSafe(tradeableId) + "." + nullSafe(clearingAccountId) + "." + nullSafe(status) + "."
				+ nullSafe(clearedTradeId);
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
	public String getClearingAccountId() {
		return clearingAccountId;
	}

	public void setClearingAccountId(String accountId) {
		this.clearingAccountId = accountId;
	}

	@Property
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Property
	public String getClearingFeeCurrency() {
		return clearingFeeCurrency;
	}

	public void setClearingFeeCurrency(String clearingFeeCurrency) {
		this.clearingFeeCurrency = clearingFeeCurrency;
	}

	@Property
	public String getExchangeFeeCurrency() {
		return exchangeFeeCurrency;
	}

	public void setExchangeFeeCurrency(String exchangeFeeCurrency) {
		this.exchangeFeeCurrency = exchangeFeeCurrency;
	}

	@Property
	public String getBrokerFeeCurrency() {
		return brokerFeeCurrency;
	}

	public void setBrokerFeeCurrency(String brokerFeeCurrency) {
		this.brokerFeeCurrency = brokerFeeCurrency;
	}

	@Property
	public Double getClearingFee() {
		return clearingFee;
	}

	public void setClearingFee(Double clearingFee) {
		this.clearingFee = clearingFee;
	}

	@Property
	public Double getExchangeFee() {
		return exchangeFee;
	}

	public void setExchangeFee(Double exchangeFee) {
		this.exchangeFee = exchangeFee;
	}

	@Property
	public Double getBrokerFee() {
		return brokerFee;
	}

	public void setBrokerFee(Double brokerFee) {
		this.brokerFee = brokerFee;
	}

	@Property
	public String getClearedTradeId() {
		return clearedTradeId;
	}

	public void setClearedTradeId(String clearedTradeId) {
		this.clearedTradeId = clearedTradeId;
	}

	@Property
	public String getCusip() {
		return cusip;
	}

	public void setCusip(String cusip) {
		this.cusip = cusip;
	}

	@Property
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Property
	public String getSubAccountId() {
		return subAccountId;
	}

	public void setSubAccountId(String subAccountId) {
		this.subAccountId = subAccountId;
	}

	@Property
	public Long getDate8() {
		return date8;
	}

	public void setDate8(Long date8) {
		this.date8 = date8;
	}

}
