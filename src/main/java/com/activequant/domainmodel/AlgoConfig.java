package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

/**
 * Algo Config class. Extend for your own needs. 
 * 
 * @author GhostRider
 *
 */
public class AlgoConfig extends PersistentEntity {

	private String id;
	
	public AlgoConfig(){
		super(AlgoConfig.class.getCanonicalName());
	}
	
	public AlgoConfig(String className){
		super(className);
	}
	
	@Property
	@Override
	public String getId() {		
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}
