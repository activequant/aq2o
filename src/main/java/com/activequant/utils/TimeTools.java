package com.activequant.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.activequant.domainmodel.TimeStamp;

public class TimeTools {
	
	public boolean isWeekday(TimeStamp timeStamp) {
		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("UTC"));
		cal.setTimeInMillis(timeStamp.getNanoseconds() / 1000000);
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		if (dow == Calendar.SATURDAY || dow == Calendar.SUNDAY) {
			return false;
		}
		return true;

	}

}
