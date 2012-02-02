package com.activequant.archive;

import java.util.Iterator;
import java.util.Map;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;


/**
 * 
 * @author ustaudinger
 * 
 */
public abstract class MultiValueTimeSeriesIterator implements Iterator<Tuple<TimeStamp, Map<String, Double>>>{

	/**
	 * Implemented as final to prevent re-implementation. 
	 */
	@Override
	public final void remove() {				
	}

}
