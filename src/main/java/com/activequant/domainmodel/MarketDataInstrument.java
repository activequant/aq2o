package com.activequant.domainmodel;

import com.activequant.utils.annotations.Property;

/**
 * key for this is an aim hedge instrument ID. This table is populated from
 * connectivity drivers.
 * 
 * @author ustaudinger
 * 
 */
public class MarketDataInstrument extends PersistentEntity {

    private String instrumentId, mdProvider, providerSpecificId;
    private double lastHistFetchTime = 0.0;
    private double scalingFactor = 1.0;

    public MarketDataInstrument(String className){
    	super(className);
    }
    
    public MarketDataInstrument() {
        super(MarketDataInstrument.class.getCanonicalName());
    }

    public String getId() {
        return nullSafe(mdProvider) + "_" + nullSafe(providerSpecificId);
    }

    @Override
    public String toString() {
        return nullSafe(mdProvider) + "/" + nullSafe(providerSpecificId) + "/" + nullSafe(instrumentId) + "/" + lastHistFetchTime + "/" + getSnapshotTime();
    }

    @Property
    public String getInstrumentId() {
        return instrumentId;
    }

    @Property
    public double getLastHistFetchTime() {
        return lastHistFetchTime;
    }

    @Property
    public String getMdProvider() {
        return mdProvider;
    }

    @Property
    public String getProviderSpecificId() {
        return providerSpecificId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public void setLastHistFetchTime(double lastFetchTime) {
        this.lastHistFetchTime = lastFetchTime;
    }

    public void setMdProvider(String s) {
        mdProvider = s;
    }

    public void setProviderSpecificId(String providerSpecificId) {
        this.providerSpecificId = providerSpecificId;
    }

    @Property
    public double getScalingFactor() {
        return scalingFactor;
    }

    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }
}
