package com.activequant.domainmodel.backoffice;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.annotations.Property;

public class PandS extends PersistentEntity {
	
	private String tradeableId; 	
	private Long date8;	
	private String clearingAccount;
	private String currency;
	private Double netAmount; 
	
	public PandS(){
		super(PandS.class.getCanonicalName());
	}
	@Override
	public String getId() {
		// 
		return "PNS."+clearingAccount + "."+currency+"."+tradeableId+"."+date8;
	}

	@Property
	public String getTradeableId() {
		return tradeableId;
	}


	public void setTradeableId(String tradeableId) {
		this.tradeableId = tradeableId;
	}

	@Property
	public Long getDate8() {
		return date8;
	}


	public void setDate8(Long date8) {
		this.date8 = date8;
	}

	@Property
	public String getClearingAccount() {
		return clearingAccount;
	}


	public void setClearingAccount(String clearingAccount) {
		this.clearingAccount = clearingAccount;
	}

	@Property
	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Property
	public Double getNetAmount() {
		return netAmount;
	}


	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

}
