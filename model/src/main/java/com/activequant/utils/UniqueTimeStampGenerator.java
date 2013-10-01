package com.activequant.utils;

import java.util.Date;

import com.activequant.domainmodel.TimeStamp;

/**
 * Utility class to generate unique date for market events.
 * The problem is that some feed sources do not supply milliseconds (Opentick's)
 * in the date field. Since AQ code depends on date to be unique within the
 * type and feed source, we need to fake millisecond part.
 * This object generates unique date by catching duplicates and 
 * assigning milliseconds sequentially.
 * <br>
 * Optionally, it is possible to generate a timestamp from a number like 
 * the number of milliseconds since unix epoch start. 
 * <br>
 * <b>History:</b><br>
 *  - [27.11.2007] Created (Mike Kroutikov)<br>
 *  - [26.09.2009] Adding generate(long) method (Ulrich Staudinger)<br>
 *
 *  @author Mike Kroutikov
 *  @author Ulrich Staudinger
 */
public class UniqueTimeStampGenerator {
	
	private static long AMBIGUATION_UNITS_IN_MILLIS = 1000000;
	
	private long lastMillis = 0L;
	private long nanos      = 0;
	private static UniqueTimeStampGenerator instance = null;  

	public TimeStamp now(){
		return generate(new Date());
	}
	
	public TimeStamp generate(Date input) {
		long time = input.getTime();
		return generate(time);
	}
	
	public TimeStamp generate(long inputInMilliseconds)
	{		
		if(inputInMilliseconds > lastMillis) {
			lastMillis = inputInMilliseconds;
			nanos = 0;
		} else if(inputInMilliseconds == lastMillis) {
			nanos++;
			if(nanos >= AMBIGUATION_UNITS_IN_MILLIS) {
				// can come here only if events are generated at rate one per nanosecond
				// practically impossible
				throw new AssertionError("failed to disambiguate");
			}
		} else {
			throw new AssertionError("input dates are out-of-order : "+inputInMilliseconds+" is lower than "+lastMillis);
		}

		return new TimeStamp(lastMillis * AMBIGUATION_UNITS_IN_MILLIS + nanos); // disambiguate
	}

	public static UniqueTimeStampGenerator getInstance(){
		if(instance==null)
			instance = new UniqueTimeStampGenerator();
		return instance; 
	}
	
}
