package com.activequant.domainmodel;

import com.activequant.domainmodel.annotations.Property;

public class OTCCounterparty extends Venue {

    private String contactPerson, phoneNumber, contactEmail, remarks;

    public OTCCounterparty() {
        super(OTCCounterparty.class.getCanonicalName());
    }

    public OTCCounterparty(String venueId, String country, String description, String name, String contactPerson,
            String phoneNumber, String contactEmail, String remarks) {
        super(OTCCounterparty.class.getCanonicalName(), venueId, country, description, name);
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.phoneNumber = phoneNumber;
        this.remarks = remarks;

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

    @Property
    public String getRemarks() {
        return remarks;
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

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
