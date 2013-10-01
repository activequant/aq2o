package com.activequant.domainmodel.backoffice;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.annotations.Property;

public class PNL extends PersistentEntity {
	String tradeableId;
	Long date8;
	String clearingAccountId;
	String currency;
	Double grossPNL;
	Double netPNL;

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
	public String getClearingAccountId() {
		return clearingAccountId;
	}

	public void setClearingAccountId(String clearingAccountId) {
		this.clearingAccountId = clearingAccountId;
	}

	@Property
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Property
	public Double getGrossPNL() {
		return grossPNL;
	}

	public void setGrossPNL(Double grossPNL) {
		this.grossPNL = grossPNL;
	}

	@Property
	public Double getNetPNL() {
		return netPNL;
	}

	public void setNetPNL(Double netPNL) {
		this.netPNL = netPNL;
	}

	public PNL() {
		super(PNL.class.getCanonicalName());
	}

	@Override
	public String getId() {
		//
		return "PNL." + clearingAccountId + "." + tradeableId + "." + date8;
	}

}
