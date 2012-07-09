package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

/**
 * Will go soon to another module. 
 * 
 * @author GhostRider
 *
 */
public class BloombergMarketDataInstrument extends MarketDataInstrument {

	private String globalBBId; 

	public BloombergMarketDataInstrument(){
		super(BloombergMarketDataInstrument.class.getCanonicalName());
		setMdProvider("BBGT");
	}
	
	public String getId(){
        return nullSafe(getMdProvider()) + "_" + nullSafe(getProviderSpecificId());
	}

	@Property
	public String getGlobalBBId() {
		return globalBBId;
	}

	public void setGlobalBBId(String globalBBId) {
		this.globalBBId = globalBBId;
	}
	
}
