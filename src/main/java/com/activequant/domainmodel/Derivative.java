package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public abstract class Derivative extends ListedInstrument {

    private String underlyingId;

    public Derivative(String name) {
        super(name);
    }

    @Property
    public String getUnderlyingId() {
        return underlyingId;
    }

    public void setUnderlyingId(String underlyingId) {
        this.underlyingId = underlyingId;
    }

}
