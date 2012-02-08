package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public class FX extends Instrument {

    private String from, to;

    public FX() {
        super(FX.class.getCanonicalName());
    }

    @Property
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Property
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String getId() {
        return "FX." + nullSafe(getName());
    }

}
