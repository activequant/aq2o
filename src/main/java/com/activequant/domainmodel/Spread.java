package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;


public class Spread extends Instrument {

    private String buySideInstrumentId, sellSideInstrumentId;
    private String spreadType; 

    public Spread() {
        super(Spread.class.getCanonicalName());
    }
    
    @Override
    public String getId() {
        return "SPREAD." + nullSafe(buySideInstrumentId)+"."+nullSafe(sellSideInstrumentId)+"."+nullSafe(spreadType);
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

}
