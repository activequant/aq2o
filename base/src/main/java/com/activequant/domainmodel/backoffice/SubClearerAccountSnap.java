package com.activequant.domainmodel.backoffice;

import com.activequant.domainmodel.annotations.Property;


public class SubClearerAccountSnap extends Snapshot {

	private String subClearerAccountId;
	@Property
	public String getSubClearerAccountId() {
		return subClearerAccountId;
	}

	public void setSubClearerAccountId(String subCashAccountId) {
		this.subClearerAccountId = subCashAccountId;
	}

	@Property
	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Property
	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	private String currency;
	private Double amount;


	
	@Override
	@Property
	public String getNonUniqueID() {
		return "SCAS." + nullSafe(subClearerAccountId) + "." + nullSafe(currency);
	}
	
	
	public SubClearerAccountSnap() {
		super(SubClearerAccountSnap.class.getCanonicalName());
	}

}
