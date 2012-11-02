package com.activequant.domainmodel.backoffice;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.annotations.Property;

public abstract class Account extends PersistentEntity {
    
    private String accountId; 
    private String legalEntity;
    private String contactPerson; 
    
    
    public Account(String className){
        super(className);
    }
    
    @Property
    public String getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(String name) {
        this.legalEntity = name;
    }

    @Property
    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Property
    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    } 
    
    
    
    
    
}
