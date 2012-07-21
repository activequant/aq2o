package com.activequant;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.domainmodel.Country;
import com.activequant.domainmodel.SecurityChainByDate;
import com.activequant.interfaces.dao.ICountryDao;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.ISecurityChainDao;

/**
 * Unit test for simple App.
 */
public class SecurityChainDaoTest extends TestCase {
    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public SecurityChainDaoTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(SecurityChainDaoTest.class);
    }

    public void testCreate() throws Exception {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("springtest.xml");
        IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
        ISecurityChainDao idao = idf.securityChainDao();
     
        SecurityChainByDate sc = new SecurityChainByDate();
        sc.setChainName("TEST");
		sc.add("A", 2010L);
		assertEquals(1, sc.getRollDates().length);
		
		sc.add("B", 2011L);
		assertEquals(2, sc.getRollDates().length);
        
        idao.create(sc);

        SecurityChainByDate loaded = (SecurityChainByDate)idao.load(sc.getId());
        assertEquals(loaded.getChainName(), sc.getChainName());
        assertEquals(loaded.getValidInstrumentIDs().length, sc.getValidInstrumentIDs().length);
        assertEquals(loaded.getRollDates().length, sc.getRollDates().length);
        
        //
        idao.delete(sc);
    }
}
