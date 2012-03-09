package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public class Spread extends Instrument {

	private String buySideInstrumentId, sellSideInstrumentId;
	private Integer buySideQuantity, sellSideQuantity; 
	private String spreadType, exchange, currency;

	public Spread() {
		super(Spread.class.getCanonicalName());
	}

	@Override
	public String getId() {
		return "SPREAD." + nullSafe(super.getSymbolId()) + "." + nullSafe(super.getShortName()) + "."
				+ nullSafe(spreadType) + "." + nullSafe(exchange) + "." + nullSafe(currency);
	}

	@Property
	public String getBuySideInstrumentId() {
		return buySideInstrumentId;
	}

	public void setBuySideInstrumentId(String leg1Id) {
		this.buySideInstrumentId = leg1Id;
	}

	@Property
	public String getSellSideInstrumentId() {
		return sellSideInstrumentId;
	}

	public void setSellSideInstrumentId(String leg2Id) {
		this.sellSideInstrumentId = leg2Id;
	}

	@Property
	public String getSpreadType() {
		return spreadType;
	}

	public void setSpreadType(String spreadType) {
		this.spreadType = spreadType;
	}

	@Property
	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	@Property
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Property
	public Integer getBuySideQuantity() {
		return buySideQuantity;
	}

	public void setBuySideQuantity(Integer buySideQuantity) {
		this.buySideQuantity = buySideQuantity;
	}
	@Property
	public Integer getSellSideQuantity() {
		return sellSideQuantity;
	}

	public void setSellSideQuantity(Integer sellSideQuantity) {
		this.sellSideQuantity = sellSideQuantity;
	}

}
