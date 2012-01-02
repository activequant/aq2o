package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public class Position extends PersistentEntity {

    private double avgOpenPrice;

    private String brokerId;
    private String instrumentId;
    private String portfolioId;
    private double quantity;
    private double valuationPrice;

    public Position() {
        super(Position.class.getCanonicalName());
    }

    @Property
    public double getAvgOpenPrice() {
        return avgOpenPrice;
    }

    @Property
    public String getBrokerId() {
        return brokerId;
    }

    @Override
    public String getId() {
        return nullSafe(portfolioId) + "." + nullSafe(brokerId) + "." + nullSafe(instrumentId);
    }

    @Property
    public String getInstrumentId() {
        return instrumentId;
    }

    @Property
    public String getPortfolioId() {
        return portfolioId;
    }

    @Property
    public double getQuantity() {
        return quantity;
    }

    @Property
    public double getValuationPrice() {
        return valuationPrice;
    }

    public void setAvgOpenPrice(double avgOpenPrice) {
        this.avgOpenPrice = avgOpenPrice;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setValuationPrice(double valuationPrice) {
        this.valuationPrice = valuationPrice;
    }
}
