package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public class Portfolio extends PersistentEntity {

    private String accountId;
    private String[] tradeableInstrumentIds;
    private double[] positions;
    private double[] entryPrice; 

    public Portfolio() {
        super(Portfolio.class.getCanonicalName());
    }

    @Property
    public String getAccountId() {
        return accountId;
    }

    @Override
    public String getId() {
        return nullSafe(accountId);
    }

    @Property
    public String[] getTradeableInstrumentIds() {
        return tradeableInstrumentIds;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setTradeableInstrumentIds(String[] positionIds) {
        this.tradeableInstrumentIds = positionIds;
    }

	public double[] getPositions() {
		return positions;
	}

	public void setPositions(double[] positions) {
		this.positions = positions;
	}

	public double[] getEntryPrice() {
		return entryPrice;
	}

	public void setEntryPrice(double[] entryPrice) {
		this.entryPrice = entryPrice;
	}
}
