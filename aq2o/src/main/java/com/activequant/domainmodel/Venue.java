package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public abstract class Venue extends PersistentEntity {
    private String venueId, country, description, name;

    public Venue(String className) {
        super(className);
    }

    public Venue(String className, String venuId, String country, String description, String name) {
        super(className);
        this.venueId = venuId;
        this.country = country;
        this.description = description;
        this.name = name;
    }

    public String getId() {
        return getVenueId();
    }

    @Property()
    public String getCountry() {
        return country;
    }

    @Property()
    public String getDescription() {
        return description;
    }

    @Property()
    public String getName() {
        return name;
    }

    @Property()
    public String getVenueId() {
        return venueId;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

}
