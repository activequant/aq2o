package com.activequant.domainmodel;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.activequant.exceptions.InvalidDate8Time6Input;
import com.activequant.utils.Date8Time6Parser;

public class Date8Time6 {
	private final double timeStamp;
	private final long microseconds;

	public Date8Time6(double timeStamp) throws InvalidDate8Time6Input {
		this.timeStamp = timeStamp;
		microseconds = new Date8Time6Parser().getMicroseconds(timeStamp);
	}

	public final double doubleValue() {
		return timeStamp;
	}

	public final long asMicroSeconds() {
		return microseconds;
	}

	public final long asMilliSeconds() {
		return microseconds / 1000L;
	}

	public final Date asDate() {
		return new Date(asMilliSeconds());
	}

	public boolean isWeekday() {
		Calendar cal = GregorianCalendar.getInstance(TimeZone
				.getTimeZone("UTC"));
		cal.setTimeInMillis(asMilliSeconds());
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		if (dow == Calendar.SATURDAY || dow == Calendar.SUNDAY) {
			return false;
		}
		return true;

	}

	public final String asString() {
		DecimalFormat dcf = new DecimalFormat("00000000000000.000000000");
		return dcf.format(timeStamp);

	}

	@SuppressWarnings("finally")
	public final static Date8Time6 now() {
		try {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTimeZone(TimeZone.getTimeZone("UTC"));
			return new Date8Time6(Double.parseDouble(new Date8Time6Parser()
					.format(cal.getTime())));
			// will not and cannot be reached. 
		} finally {
			return null;
		}
	}
}
