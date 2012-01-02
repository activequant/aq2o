package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public class Portfolio extends PersistentEntity {

    private String accountId;
    private String[] positionIds;

    public Portfolio() {
        super(Portfolio.class.getCanonicalName());
    }

    @Property
    public String getAccountId() {
        return accountId;
    }

    @Override
    public String getId() {
        return nullSafe(accountId);
    }

    @Property
    public String[] getPositionIds() {
        return positionIds;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setPositionIds(String[] positionIds) {
        this.positionIds = positionIds;
    }
}
