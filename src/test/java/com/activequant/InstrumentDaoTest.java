package com.activequant;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.DaoException;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.domainmodel.FX;
import com.activequant.domainmodel.Future;

/**
 * Unit test for simple App.
 */
public class InstrumentDaoTest extends TestCase {

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(InstrumentDaoTest.class);
    }

    public void testCreateFX() throws DaoException {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("springtest.xml");
        IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
        IInstrumentDao idao = idf.instrumentDao();
        long totalRunTime = 0L;
        int n = 10;
        for (int i = 0; i < n; i++) {

            FX fx = new FX();
            fx.setCreationTime(System.currentTimeMillis());
            fx.setDeletionTime(0L);
            fx.setDescription("...");
            fx.setFrom("EUR");
            // have to make it unique, otherwise key failure
            fx.setTo("USD" + i);
            fx.setShortName("EURUSD" + i);

            long l1 = System.currentTimeMillis();
            idao.create(fx);
            long l2 = System.currentTimeMillis();

            long diff = l2 - l1;

            totalRunTime += diff;
        }
        double timePerInsert = (totalRunTime) / (double) n;
        System.out.println("Total time: " + (totalRunTime) + " ms, time per random fetch " + timePerInsert + " ms");
    }

    /**
     * test saving and then loading of a future.
     * 
     * @throws DaoException
     */
    public void testCreateFuture() throws DaoException {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("springtest.xml");
        IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
        IInstrumentDao idao = idf.instrumentDao();

        Future future = new Future();
        future.setCreationTime(0L);
        future.setDeletionTime(0L);
        future.setName("FDAX");
        future.setDescription("The dax future");
        future.setExpiry(20111231l);
        future.setShortName("FDAX");
        future.setTickSize(10.0);
        future.setTickValue(10.0);
        idao.create(future);

        // load the future
        Future loadedFuture = (Future) idao.load(future.getId());
        assertEquals(future.getId(), loadedFuture.getId());
        assertEquals(future.getShortName(), loadedFuture.getShortName());
        assertEquals(future.getTickSize(), loadedFuture.getTickSize());
        assertEquals(future.getTickValue(), loadedFuture.getTickValue());

    }

    public void testSearchIds() throws DaoException {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("springtest.xml");
        IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
        IInstrumentDao idao = idf.instrumentDao();

        // insert 100 instruments.
        for (int i = 0; i < 100; i++) {
            Future future = new Future();
            future.setCreationTime(0L);
            future.setDeletionTime(0L);
            future.setName("FDAX" + i);
            future.setDescription("The dax future");
            future.setExpiry(20111231l);
            future.setShortName("FDAX" + i);
            future.setTickSize(10.0);
            future.setTickValue(10.0);
            idao.create(future);
        }

        // search.
        String[] ids = idao.searchById("%.FDAX2.%", 1);
        assertEquals(1, ids.length);

        ids = idao.searchById("%FDAX2%", 100);
        assertEquals(11, ids.length);

        assertEquals("FUT.<NA>.FDAX2.20111231", ids[0]);

    }

}
