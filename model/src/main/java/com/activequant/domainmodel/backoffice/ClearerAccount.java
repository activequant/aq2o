package com.activequant.domainmodel.backoffice;

import com.activequant.domainmodel.annotations.Property;


public class ClearerAccount extends Account {
    
	private String customerFundId; 
	
    public ClearerAccount(){
        super(ClearerAccount.class.getCanonicalName());
    }
    
    @Override
    public String getId() {
        return "CLRACT."+nullSafe(getAccountId())+"."+nullSafe(getLegalEntity());
    }

    @Property
	public String getCustomerFundId() {
		return customerFundId;
	}

	public void setCustomerFundId(String customerFundId) {
		this.customerFundId = customerFundId;
	}
    
}
