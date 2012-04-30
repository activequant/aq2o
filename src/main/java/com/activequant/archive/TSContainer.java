package com.activequant.archive;

import com.activequant.domainmodel.TimeStamp;

/**
 * Trivial container class, even without getters and setters.
 * 
 * @author ustaudinger
 * 
 */
public class TSContainer {

    public TimeStamp[] timeStamps;
    public Double[] values;

    public TSContainer(TimeStamp[] ts, Double[] v) {
        timeStamps = ts;
        values = v;
    }
    

    public Double getElementAtDate(TimeStamp ts){
    	return null; 
    }
    
    public Long getPosition(TimeStamp ts){
    	return null; 
    }    
    
}
