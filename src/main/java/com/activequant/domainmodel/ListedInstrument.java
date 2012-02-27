package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public abstract class ListedInstrument extends Instrument {

    private String currency;
    private String exchangeCode;
    private String quoteUnits;
    private Double tickValue;
    private Double tickSize;
    private String cusip; 

    @Property()
    public Double getTickSize() {
        return tickSize;
    }

    public void setTickSize(Double tickSize) {
        this.tickSize = tickSize;
    }

    @Property
    public Double getTickValue() {
        return tickValue;
    }

    public void setTickValue(Double tickValue) {
        this.tickValue = tickValue;
    }

    @Property
    public String getQuoteUnits() {
        return quoteUnits;
    }

    public void setQuoteUnits(String quoteUnits) {
        this.quoteUnits = quoteUnits;
    }

    @Property
    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    @Property
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ListedInstrument(String name) {
        super(name);
    }

    @Property
    public String getCusip() {
		return cusip;
	}

	public void setCusip(String cusip) {
		this.cusip = cusip;
	}

}
