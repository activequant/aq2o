package com.activequant.dto;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class Util {
	
	public String ensureDoubleDigitBBID(String singleOrDoubleDigitBBId) {
		Calendar cal = GregorianCalendar.getInstance();
		int currentYearEndDigit = cal.get(Calendar.YEAR) % 10;
		int currentDecadeDigit = (int) Math.floor(cal.get(Calendar.YEAR) % 100 / 10);
		for (int repYear = 0; repYear < 10; repYear++) {
		    if (repYear == 10)
		        break;
		    String replacer = "" + (currentDecadeDigit + 1);
		    if (repYear >= currentYearEndDigit)
		        replacer = "" + currentDecadeDigit;
		    Pattern p = Pattern.compile("[A-Z]" + repYear + "");
		    if (p.matcher(singleOrDoubleDigitBBId).find())
		    	singleOrDoubleDigitBBId = singleOrDoubleDigitBBId.replaceAll("" + repYear, replacer + repYear);
		}
		return singleOrDoubleDigitBBId;
	}
	
}

