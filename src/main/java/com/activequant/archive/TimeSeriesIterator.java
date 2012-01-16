package com.activequant.archive;

import java.util.Iterator;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;


/**
 * 
 * @author ustaudinger
 * 
 */
public abstract class TimeSeriesIterator implements Iterator<Tuple<TimeStamp, Double>>{

	/**
	 * Implemented as final to prevent re-implementation. 
	 */
	@Override
	public final void remove() {				
	}

}
