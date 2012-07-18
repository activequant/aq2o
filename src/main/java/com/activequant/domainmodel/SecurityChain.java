package com.activequant.domainmodel;

import com.activequant.domainmodel.annotations.Property;

public abstract class SecurityChain extends PersistentEntity {

	private String chainName; 

	public SecurityChain(String className){
		super(className);
	}
	
	@Property
	public String getChainName() {
		return chainName;
	}


	public void setChainName(String chainName) {
		this.chainName = chainName;
	}
}
