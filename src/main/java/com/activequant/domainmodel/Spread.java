package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public class Spread extends Instrument {

	private String instrument1, instrument2;
	private Integer quantity1, quantity2;
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
	public String getInstrument1() {
		return instrument1;
	}

	public void setInstrument1(String instrument1) {
		this.instrument1 = instrument1;
	}

	@Property
	public String getInstrument2() {
		return instrument2;
	}

	public void setInstrument2(String instrument2) {
		this.instrument2 = instrument2;
	}

	@Property
	public Integer getQuantity1() {
		return quantity1;
	}

	public void setQuantity1(Integer quantity1) {
		this.quantity1 = quantity1;
	}

	@Property
	public Integer getQuantity2() {
		return quantity2;
	}

	public void setQuantity2(Integer quantity2) {
		this.quantity2 = quantity2;
	}

}
