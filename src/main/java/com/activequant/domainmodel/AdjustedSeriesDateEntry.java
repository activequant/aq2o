package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

// 
public class AdjustedSeriesDateEntry extends PersistentEntity {

	// 
	private String chainName; 
	private Long date; 
	private String instrumentId;
	
	// 
	public AdjustedSeriesDateEntry(){
		super(AdjustedSeriesDateEntry.class.getCanonicalName());
	}
	
	@Override
	public String getId() {
		return nullSafe(chainName)+"."+nullSafe(date);
	}

	@Property
	public String getChainName() {
		return chainName;
	}

	public void setChainName(String chainName) {
		this.chainName = chainName;
	}

	@Property
	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}
	
	@Property
	public String getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	} 
	
	// 
	
}
