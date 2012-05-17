package com.activequant.timeseries;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.domainmodel.TimeStamp;

public class TSContainerMethodsTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public TSContainerMethodsTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TSContainerMethodsTest.class);
    }
    
    public void test(){
    	TSContainer2 in = new TSContainer2("Test", Arrays.asList(new String[]{"A"}), Arrays.asList(new TypedColumn[]{new DoubleColumn()}));
    	in.setRow(new TimeStamp(5L), 1.0);
    	in.setRow(new TimeStamp(10L), 2.0);
    	in.setRow(new TimeStamp(15L), 3.0);
    	in.setRow(new TimeStamp(20L), 4.0);
    	in.setRow(new TimeStamp(25L), 5.0);
    	in.setRow(new TimeStamp(30L), 6.0);
    	
    	assertEquals(1.0, in.getRow(new TimeStamp(5L))[0]);
    	assertEquals(2.0, in.getRow(new TimeStamp(10L))[0]);
    	
    	TSContainerMethods tcm = new TSContainerMethods();
    	TSContainer2 resampled1 = tcm.resample(in, 1L);
    	assertEquals(in.getNumRows(), resampled1.getNumRows());
    }

}
