package com.activequant.archive;

/**
 * Trivial container class, even without getters and setters.
 * 
 * @author ustaudinger
 * 
 */
public class TSContainer {

    // timestamps are date8time6 double values.
    public Double[] timeStamps;
    public Double[] values;

    public TSContainer(Double[] ts, Double[] v) {
        timeStamps = ts;
        values = v;
    }
}
