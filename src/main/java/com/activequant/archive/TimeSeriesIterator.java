package com.activequant.archive;

import java.util.Iterator;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.Tuple;


/**
 * 
 * @author ustaudinger
 * 
 */
public abstract class TimeSeriesIterator implements Iterator<Tuple<Date8Time6, Double>>{

	@Override
	public final void remove() {				
	}

}
