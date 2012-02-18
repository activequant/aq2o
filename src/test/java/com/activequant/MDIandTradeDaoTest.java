package com.activequant;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.DaoException;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.dao.ITradeableInstrumentDao;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TradeableInstrument;

/**
 * Unit test for simple App.
 */
public class MDIandTradeDaoTest extends TestCase {

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(MDIandTradeDaoTest.class);
	}

	public void testMDIFindByProvId() throws DaoException {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("springtest.xml");
		IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
		IMarketDataInstrumentDao idao = idf.mdiDao();
		
		MarketDataInstrument mdi = new MarketDataInstrument();
		mdi.setProviderSpecificId("ABCD");
		mdi.setMdProvider("BBGT");
		idao.create(mdi);
		
		MarketDataInstrument mdi2 = idao.findByProvId("BBGT", "ABCD");
		assertNotNull(mdi2);
		assertEquals(mdi.getProviderSpecificId(), mdi2.getProviderSpecificId());
		assertEquals(mdi.getMdProvider(), mdi2.getMdProvider());
		
	}

	public void testTradeableFindByProvId() throws DaoException {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("springtest.xml");
		IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
		ITradeableInstrumentDao idao = idf.tradeableDao();
		
		TradeableInstrument mdi = new TradeableInstrument();
		mdi.setProviderSpecificId("ABCD");
		mdi.setTradingProvider("BBGT");
		idao.create(mdi);
		
		TradeableInstrument mdi2 = idao.findByProvId("BBGT", "ABCD");
		assertNotNull(mdi2);
		assertEquals(mdi.getProviderSpecificId(), mdi2.getProviderSpecificId());
		assertEquals(mdi.getTradingProvider(), mdi2.getTradingProvider());
		
	}

}
