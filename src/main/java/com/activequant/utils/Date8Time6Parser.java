package com.activequant.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import com.activequant.exceptions.InvalidDate8Time6Input;

/**
 * Convenience class.
 * 
 * @author ustaudinger
 * 
 */
public class Date8Time6Parser extends SimpleDateFormat {

    private static final long serialVersionUID = 1L;
    private DecimalFormat dcf;

    public Date8Time6Parser() {
        super("yyyyMMddHHmmss.SSS");
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        otherSymbols.setDecimalSeparator('.');
        dcf = new DecimalFormat("00000000000000.000000000", otherSymbols);
        setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public long getMicroseconds(Double d8t6) throws InvalidDate8Time6Input {
        String s1 = dcf.format(d8t6);
        Date d1;
        try {
            d1 = parse(s1);
        } catch (ParseException e) {
            throw new InvalidDate8Time6Input("Invalid input: " + d8t6);
        }
        long ms = d1.getTime();
        int i1 = (int) Math.floor(d8t6);
        double remainder = d8t6 - (double) i1;
        remainder *= 1000.0;
        remainder %= 1.0;
        double micros = remainder * 1000.0;
        return ms * 1000L + (long) micros;

    }

    public long getNanoseconds(Double d8t6) throws InvalidDate8Time6Input {
        return getMicroseconds(d8t6) * 1000L; 
    }
    
    public Double fromMilliseconds(long ms) throws InvalidDate8Time6Input {
        String d = new Date8Time6Parser().format(new Date(ms));
        return Double.parseDouble(d);
    }

    public Double now() throws InvalidDate8Time6Input {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        return Double.parseDouble(new Date8Time6Parser().format(cal.getTime()));
    }
    
    public String toString(Double date8time6){
    	return dcf.format(date8time6);
    }
}