package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public class Forward extends Derivative {

	private Double lotSize;
	private Double tickSize;
	private Double tickValue;

	public Forward() {
		super(Forward.class.getCanonicalName());
	}

	public Forward(String instrumentId, String description, Double lotSize, Double tickSize, Double tickValue) {
		super(Forward.class.getCanonicalName());
		super.setShortName(instrumentId);
		super.setDescription(description);
		this.lotSize = lotSize;
		this.tickSize = tickSize;
		this.tickValue = tickValue;
	}

	public double fullPointValue() {
		return 1.0 / tickSize * tickValue;
	}

	public String getId() {
		return "FUT." + nullSafe(getExchangeCode()) + "." + nullSafe(getShortName());
	}

	@Property
	public Double getLotSize() {
		return lotSize;
	}

	@Property
	public Double getTickSize() {
		return tickSize;
	}

	@Property
	public Double getTickValue() {
		return tickValue;
	}

	public void setLotSize(Double lotSize) {
		this.lotSize = lotSize;
	}

	public void setTickSize(Double tickSize) {
		this.tickSize = tickSize;
	}

	public void setTickValue(Double tickValue) {
		this.tickValue = tickValue;
	}

}
