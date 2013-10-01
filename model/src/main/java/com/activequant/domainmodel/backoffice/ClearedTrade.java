package com.activequant.domainmodel.backoffice;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.annotations.Property;

public class ClearedTrade extends PersistentEntity {
	private String tradeableId;
	private Double price;
	private Long quantity;
	private Long timeStampInNanos;
	private String orderSide;
	private String clearingAccountId;
	private String status;
	private String clearedTradeId;
	private String clearingFeeCurrency;
	private String exchangeFeeCurrency;
	private String brokerFeeCurrency;
	private Double clearingFee;
	private Double exchangeFee;
	private Double brokerFee;
	private String cusip;
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
