package com.activequant.domainmodel.backoffice;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.annotations.Property;

public abstract class Portfolio extends PersistentEntity {

    private String name; 
    
    public Portfolio(String className){
        super(className);
    }



	@Property
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}   
    
    
}
