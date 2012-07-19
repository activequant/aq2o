package com.activequant.domainmodel;

import com.activequant.domainmodel.annotations.Property;

/**
 * Algo Config class. Extend for your own needs. 
 * 
 * @author GhostRider
 *
 */
public class AlgoConfig extends PersistentEntity implements Cloneable {

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
	
	/**
	 * Reimplement in your own class. 
	 *  
	 */
	public AlgoConfig clone(){
		AlgoConfig clone = new AlgoConfig();
		clone.setId(id);
		return clone; 
	}
}
