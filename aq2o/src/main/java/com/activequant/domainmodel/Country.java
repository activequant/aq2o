package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

/**
 * Just mark all properties as @Property.
 * 
 * @author ustaudinger
 * 
 */
public class Country extends PersistentEntity {

    private String country, currency, region;

    public Country() {
        super(Country.class.getCanonicalName());
    }

    public String getId() {
        return getCountry();
    }

    @Property()
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Property()
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Property()
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}
