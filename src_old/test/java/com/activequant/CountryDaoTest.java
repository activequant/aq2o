package com.activequant;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.domainmodel.Country;
import com.activequant.interfaces.dao.ICountryDao;
import com.activequant.interfaces.dao.IDaoFactory;

/**
 * Unit test for simple App.
 */
public class CountryDaoTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public CountryDaoTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(CountryDaoTest.class);
    }

    public void testCreate() throws Exception {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("springtest.xml");
        IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
        ICountryDao idao = idf.countryDao();
        Country c = new Country();
        c.setCountry("EUR");
        c.setCurrency("EUR");
        c.setRegion("EU");
        idao.create(c);

        //
        idao.delete(c);
    }

    public void testUpdate() throws Exception {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("springtest.xml");
        IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
        ICountryDao idao = idf.countryDao();
        Country c = new Country();
        c.setCountry("EUR");
        c.setCurrency("EUR");
        c.setRegion("EU");
        idao.create(c);
        // now let's update it.

        c.setCountry("FRA");

        // won't work with hsqldb
        // idao.update(c);

        //
        idao.delete(c);
    }

}
