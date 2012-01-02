package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public class SuperInstrument extends Instrument {

    private Long[] rollDates;
    private Double tickSize, tickValue;
    private String type, exchange, currency, csiSymbol, twSymbol, aimSymbol, bloombergSymbol, bbYellowKey;
    private String[] validInstrument;

    public SuperInstrument() {
        super(SuperInstrument.class.getCanonicalName());
    }

    public RollingSchedule rollingSchedule() {
        if (rollDates == null || validInstrument == null) {
            rollDates = new Long[0];
            validInstrument = new String[0];
        }
        assert (rollDates.length == validInstrument.length);
        return new RollingSchedule(rollDates, validInstrument);

    }

    @Property()
    public String getAimSymbol() {
        return aimSymbol;
    }

    @Property()
    public String getBbYellowKey() {
        return bbYellowKey;
    }

    @Property()
    public String getBloombergSymbol() {
        return bloombergSymbol;
    }

    @Property()
    public String getCsiSymbol() {
        return csiSymbol;
    }

    @Property()
    public String getCurrency() {
        return currency;
    }

    @Property()
    public String getExchange() {
        return exchange;
    }

    public Double getFullPointValue() {
        assert (tickSize != null);
        assert (tickValue != null);
        return 1.0 / tickSize * tickValue;
    }

    public String getId() {
        return "SUP_INST_" + getName() + "." + type + "." + exchange + "." + currency;
    }

    @Property()
    public Long[] getRollDates() {
        return rollDates;
    }

    @Property()
    public Double getTickSize() {
        return tickSize;
    }

    @Property()
    public Double getTickValue() {
        return tickValue;
    }

    @Property()
    public String getTwSymbol() {
        return twSymbol;
    }

    @Property()
    public String getType() {
        return type;
    }

    @Property()
    public String[] getValidInstrument() {
        return validInstrument;
    }

    public void setAimSymbol(String aimSymbol) {
        this.aimSymbol = aimSymbol;
    }

    public void setBbYellowKey(String bbYellowKey) {
        this.bbYellowKey = bbYellowKey;
    }

    public void setBloombergSymbol(String bloombergSymbol) {
        this.bloombergSymbol = bloombergSymbol;
    }

    public void setCsiSymbol(String csiSymbol) {
        this.csiSymbol = csiSymbol;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setRollDates(Long[] rollDates) {
        this.rollDates = rollDates;
    }

    public void setTickSize(Double tickSize) {
        this.tickSize = tickSize;
    }

    public void setTickValue(Double tickVal) {
        this.tickValue = tickVal;
    }

    public void setTwSymbol(String twSymbol) {
        this.twSymbol = twSymbol;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValidInstrument(String[] validInstrument) {
        this.validInstrument = validInstrument;
    }

    public String toString() {
        return getId() + "/" + getName();
    }
}
