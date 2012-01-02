package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

/**
 * Holds all parameters for an exchange.
 * 
 * 
 * @author ustaudinger
 * 
 */
public class Exchange extends Venue {

    private String contactPerson, phoneNumber, contactEmail;

    public Exchange() {
        super(Exchange.class.getCanonicalName());
    }

    public Exchange(String venueId, String country, String description, String name, String contactPerson,
            String phoneNumber, String contactEmail) {
        super(Exchange.class.getCanonicalName(), venueId, country, description, name);
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.phoneNumber = phoneNumber;

    }

    @Property
    public String getContactEmail() {
        return contactEmail;
    }

    @Property
    public String getContactPerson() {
        return contactPerson;
    }

    @Property
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setPhoneNumber(String phoneNumer) {
        this.phoneNumber = phoneNumer;
    }

}
