package com.activequant;

import java.io.ByteArrayInputStream;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.utils.CsvMapReader;
import com.activequant.utils.events.IEventListener;

/**
 * Unit test for simple App.
 */
public class CsvMapReaderTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public CsvMapReaderTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(CsvMapReaderTest.class);
    }

    private int rowCount = 0; 
    public void testReader() throws Exception{
    	rowCount = 0; 
    	String data = "A,B,C,D\n1,2,3,4\n2,3,4,5";    	
    	new CsvMapReader().read(new IEventListener<Map<String,String>>() {			
			@Override
			public void eventFired(Map<String, String> event) {
				rowCount++; 
				if(rowCount == 1){
					assertEquals("1", event.get("A"));
					assertEquals("4", event.get("D"));
				}
				else if(rowCount == 2){
					assertEquals("2", event.get("A"));
					assertEquals("5", event.get("D"));
				} 
			}
		}, new ByteArrayInputStream(data.getBytes()));
    	assertEquals(2, rowCount);
    }

}
