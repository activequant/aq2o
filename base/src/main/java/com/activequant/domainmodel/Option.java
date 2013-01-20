package com.activequant.domainmodel;

import com.activequant.domainmodel.annotations.Property;

public class Option extends Derivative {

	private Long expiry;
	private Long firstNotice;
	private Double lotSize;
	private String maturity;
	private String putOrCall;
	private Double strike;

	public Option() {
		super(Option.class.getCanonicalName());
	}

	@Property
	public Long getExpiry() {
		return expiry;
	}

	@Property
	public Long getFirstNotice() {
		return firstNotice;
	}

	@Override
	public String getId() {
		return "FUT." + nullSafe(getExchangeCode()) + "."
				+ nullSafe(getShortName()) + "." + nullSafe(expiry) + "."
				+ nullSafe(putOrCall) + "." + nullSafe(strike);		
	}

	@Property
	public Double getLotSize() {
		return lotSize;
	}

	@Property
	public String getMaturity() {
		return maturity;
	}

	@Property
	public String getPutOrCall() {
		return putOrCall;
	}

	@Property
	public Double getStrike() {
		return strike;
	}

	public void setExpiry(Long expiry) {
		this.expiry = expiry;
	}

	public void setFirstNotice(Long firstNotice) {
		this.firstNotice = firstNotice;
	}

	public void setLotSize(Double lotSize) {
		this.lotSize = lotSize;
	}

	public void setMaturity(String maturity) {
		this.maturity = maturity;
	}

	public void setPutOrCall(String putOrCall) {
		this.putOrCall = putOrCall;
	}

	public void setStrike(Double strike) {
		this.strike = strike;
	}

}
