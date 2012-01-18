package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public class SecurityChainByTradingDate extends SecurityChain {
	
	private String chainName; 
    private Long[] rollDates;
    private String[] validInstrumentId;
    
    public SecurityChainByTradingDate() {
        super(SecurityChainByTradingDate.class.getCanonicalName());
    }
    
	@Override
	public String getId() {
		return "SECCHAIN.DATE."+chainName;
	}

	@Property()
	public Long[] getRollDates() {
		return rollDates;
	}

	public void setRollDates(Long[] rollDates) {
		this.rollDates = rollDates;
	}

	@Property()
	public String[] getValidInstrument() {
		return validInstrumentId;
	}

	public void setValidInstrumentId(String[] validInstrument) {
		this.validInstrumentId = validInstrument;
	}

}
