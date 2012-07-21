package com.activequant;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.domainmodel.Country;
import com.activequant.domainmodel.PersistentEntity;

/**
 * Unit test for simple App.
 */
public class PersistentEntityMapTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public PersistentEntityMapTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PersistentEntityMapTest.class);
    }

    public void testApp() {
        assertTrue(true);
    }

    public void testClassName() {
        String className = ((PersistentEntity) new Country()).getClass().getCanonicalName();
        assertEquals("com.activequant.domainmodel.Country", className);
    }

    public void testCountryMapAccess() {
        Country c = new Country();
        c.setCountry("DE");
        c.setCurrency("EUR");
        c.setRegion("EU");

        Map<String, Object> map = c.propertyMap();
        assertEquals(7, map.size());

        Country c1 = new Country();
        c1.initFromMap(map);
        assertEquals("DE", c1.getCountry());
        assertEquals("EUR", c1.getCurrency());
        assertEquals("EU", c1.getRegion());

    }

}
