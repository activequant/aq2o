package com.activequant.domainmodel.backoffice;

import com.activequant.domainmodel.annotations.Property;


public class ClearerAccountSnap extends Snapshot {

	public String accountId; 
	public String subAccountId; 
	public String currency;
	public long date8;
	public String assumedTargetCurrency; 
	public double crossRate;
	public double initialMargin; 
	public double maintenanceMargin; 
	public double availableMargin; 
	public double beginningAccountBalance;
	public double endingAccountBalance; 
	public double openTradeEquity; 
	public double totalEquity; 

	public ClearerAccountSnap() {
        super(ClearerAccountSnap.class.getCanonicalName());
    }


    

    @Override
	@Property
	public String getNonUniqueID() {
		return "CAS."+nullSafe(accountId) + "." + nullSafe(subAccountId) + "."+ date8;
	}

    @Property
	public String getAccountId() {
		return accountId;
	}


	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Property
	public String getSubAccountId() {
		return subAccountId;
	}


	public void setSubAccountId(String subAccountId) {
		this.subAccountId = subAccountId;
	}

	@Property
	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}

	

	@Property
	public long getDate8() {
		return date8;
	}


	public void setDate8(long date8) {
		this.date8 = date8;
	}

	@Property
	public String getAssumedTargetCurrency() {
		return assumedTargetCurrency;
	}


	public void setAssumedTargetCurrency(String assumedTargetCurrency) {
		this.assumedTargetCurrency = assumedTargetCurrency;
	}

	@Property
	public double getCrossRate() {
		return crossRate;
	}


	public void setCrossRate(double crossRate) {
		this.crossRate = crossRate;
	}

	@Property
	public double getInitialMargin() {
		return initialMargin;
	}


	public void setInitialMargin(double initialMargin) {
		this.initialMargin = initialMargin;
	}

	@Property
	public double getMaintenanceMargin() {
		return maintenanceMargin;
	}


	public void setMaintenanceMargin(double maintenanceMargin) {
		this.maintenanceMargin = maintenanceMargin;
	}

	@Property
	public double getAvailableMargin() {
		return availableMargin;
	}


	public void setAvailableMargin(double availableMargin) {
		this.availableMargin = availableMargin;
	}

	@Property
	public double getBeginningAccountBalance() {
		return beginningAccountBalance;
	}


	public void setBeginningAccountBalance(double beginningAccountBalance) {
		this.beginningAccountBalance = beginningAccountBalance;
	}

	@Property
	public double getEndingAccountBalance() {
		return endingAccountBalance;
	}


	public void setEndingAccountBalance(double endingAccountBalance) {
		this.endingAccountBalance = endingAccountBalance;
	}

	@Property
	public double getOpenTradeEquity() {
		return openTradeEquity;
	}


	public void setOpenTradeEquity(double openTradeEquity) {
		this.openTradeEquity = openTradeEquity;
	}

	@Property
	public double getTotalEquity() {
		return totalEquity;
	}


	public void setTotalEquity(double totalEquity) {
		this.totalEquity = totalEquity;
	}

}
