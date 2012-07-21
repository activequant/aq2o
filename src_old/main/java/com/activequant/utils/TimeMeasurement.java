package com.activequant.utils;

import java.util.HashMap;

/**
 * Allows basic runtime measurements<br>
 * <br>
 * History:<br>
 *  - [10.10.2009] Created (Ghost Rider)<br>
 *  <br> 
 * @author Ghost Rider
 *
 */
public class TimeMeasurement {

	public static void start(String timerName)
	{
		startTimes.put(timerName, System.currentTimeMillis());
	}
	
	public static void stop(String timerName)
	{
		stopTimes.put(timerName, System.currentTimeMillis());
	}
	
	public static long getRuntime(String timerName)
	{
		return stopTimes.get(timerName) - startTimes.get(timerName);
	}
	
	private static HashMap<String, Long> startTimes = new HashMap<String, Long>();
	private static HashMap<String, Long> stopTimes = new HashMap<String, Long>();
}
