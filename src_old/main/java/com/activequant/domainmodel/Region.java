package com.activequant.domainmodel;

import com.activequant.domainmodel.annotations.Property;

/**
 * Just mark all properties as @Property.
 * 
 * @author ustaudinger
 * 
 */
public class Region extends PersistentEntity {

    private String region, name, description;

    public Region() {
        super(Region.class.getCanonicalName());
    }

    public String getId() {
        return getRegion();
    }

    @Property()
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Property()
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Property()
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
