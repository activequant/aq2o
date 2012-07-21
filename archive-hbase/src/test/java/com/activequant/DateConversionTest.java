package com.activequant;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.utils.Date8Time6Parser;

/**
 * Unit test for simple App.
 */
public class DateConversionTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public DateConversionTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DateConversionTest.class);
    }

    public void testCreate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String s1 = "20110101 23:59:59.990";
        double dou = 20110101235959.990;
        DecimalFormat dcf = new DecimalFormat("00000000000000.000000000");
        String s3 = dcf.format(dou);
        String shouldBe = "20110101235959.990000000";
        long ms = sdf.parse(s1).getTime();
        Date d1 = new Date(ms);
        String s2 = sdf.format(d1);
        Date8Time6Parser d8t6p = new Date8Time6Parser();
        Double d8t6 = d8t6p.fromMilliseconds(ms);
        assertEquals(shouldBe, d8t6p.toString(d8t6));

    }

}
