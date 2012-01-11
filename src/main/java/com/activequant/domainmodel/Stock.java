package com.activequant.domainmodel;

public class Stock extends ListedInstrument {

    public Stock() {
        super(Stock.class.getCanonicalName());
    }

    public Stock(String instrumentId, String description) {
        super(Stock.class.getCanonicalName());
        super.setShortName(instrumentId);
        super.setDescription(description);

    }

    public String getId() {
        return "STOCK." + nullSafe(getShortName())+"."+nullSafe(getExchangeCode());
    }

}
