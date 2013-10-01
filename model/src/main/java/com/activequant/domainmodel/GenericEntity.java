package com.activequant.domainmodel;

/**
 * a generic entity is a final class that is purely map based. The
 * PersistentEntity's underlying map has to be used.
 * 
 * Object IDs of generic entity are always prefixed by "GENERIC". 
 * 
 * @author GhostRider
 * 
 */
public final class GenericEntity extends PersistentEntity {

	private String id;

	public GenericEntity() {
		super(GenericEntity.class.getCanonicalName());
	}

	public String getId() {
		return "GENERIC."+id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
