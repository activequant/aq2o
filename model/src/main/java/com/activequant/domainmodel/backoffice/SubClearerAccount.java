package com.activequant.domainmodel.backoffice;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.annotations.Property;

public class SubClearerAccount extends PersistentEntity {

	private String clearerAcctObjId;
	private String subAcctId; 
	private String currency; 
	
	public SubClearerAccount(){
		super(SubClearerAccount.class.getCanonicalName());
	}
	
	@Override
	public String getId() {
		return "SCC."+nullSafe(clearerAcctObjId) + "." + nullSafe(subAcctId); 
	}
	
	@Property
	public String getClearerAcctObjId() {
		return clearerAcctObjId;
	}

	public void setClearerAcctObjId(String clearerAcctObjId) {
		this.clearerAcctObjId = clearerAcctObjId;
	}

	@Property
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Property
	public String getSubAcctId() {
		return subAcctId;
	}

	public void setSubAcctId(String subAcctId) {
		this.subAcctId = subAcctId;
	}

}
