package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

public abstract class Instrument extends PersistentEntity {

    private String shortName, name, description, symbol;

    public Instrument() {
        super(Instrument.class.getCanonicalName());
    }

    public Instrument(String className) {
        super(className);
    }

    @Property()
    public String getDescription() {
        return description;
    }

    public abstract String getId();

    @Property()
    public String getShortName() {
        return shortName;
    }

    /**
     * Name contains the generic name of an instrument. In case of futures, the
     * combination of name, exchange and underlying is ment to be used to match
     * futures together/to group them as belonging to the same underlying.
     * 
     * @return
     */
    @Property()
    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShortName(String instrument) {
        this.shortName = instrument;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return getId() + "/" + name;
    }

    @Property
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
