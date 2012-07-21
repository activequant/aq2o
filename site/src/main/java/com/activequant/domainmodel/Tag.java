package com.activequant.domainmodel;

import com.activequant.domainmodel.annotations.Property;

public class Tag extends PersistentEntity {

	private String tag;
	private String objectType; 
	private String objectId; 
	
	public Tag()
	{
		super(Tag.class.getCanonicalName());
	}
	
	@Override
	public String getId() {
		return nullSafe(tag) + "." 
				+ nullSafe(objectType) 
				+ "." + nullSafe(objectId);
	}
	
	@Property
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Property
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@Property
	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	
	
}
