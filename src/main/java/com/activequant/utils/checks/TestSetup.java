package com.activequant.utils.checks;

import java.net.Socket;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.utils.Date8Time6Parser;

/**
 * Main App.
 */
public class TestSetup {

	public static void p(String s) {
		System.out.println(s);
	}

	public static void main(String[] args) throws Exception {

		p("************************************");
		p("Checking your setup. Please be patient. ");
		p("************************************");

		p("Checking if your database is running at what you specified in your framework properties. ");

		Properties properties = new Properties();
		properties.load(ClassLoader.getSystemResourceAsStream("framework.properties"));

		String dbUrl = properties.getProperty("db.url");
		String host = dbUrl.substring(dbUrl.indexOf("//") + 2, dbUrl.lastIndexOf(":"));
		p("Your host runs at: " + host);
		String port = dbUrl.substring(dbUrl.lastIndexOf(":") + 1, dbUrl.lastIndexOf("/"));
		p("Your port is: " + port);
		p("You are using " + properties.getProperty("db.driver") + " as a driver.");

		try {
			p("Trying to connect to database");
			Socket s = new Socket(host, Integer.parseInt(port));
			byte[] read = new byte[30];
			s.getInputStream().read(read);
			p("First 30 bytes read after opening db connection were: " + new String(read));
			p("--------------");
			p("Now let's save something in your database ... ");

			ApplicationContext appContext = new ClassPathXmlApplicationContext("fwspring.xml");
			IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
			IMarketDataInstrumentDao mdiDao = idf.mdiDao();
			MarketDataInstrument mdi = new MarketDataInstrument();
			mdi.setMdProvider("AQTEST");
			mdi.setProviderSpecificId("TEST");
			mdi.setInstrumentId("");
			mdi.setScalingFactor(Math.random());
			mdi.setLastHistFetchTime(new Date8Time6Parser().now());

			p("Saving ...");
			mdiDao.update(mdi);
			p("Successfully saved instrument AQTEST ... Now loading ... ");
			MarketDataInstrument mdiLoaded = mdiDao.load(mdi.getId());
			p("Loaded: " + mdiLoaded.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// p("******************** ");
		// p("Now we check your HBase setup. ");
		// try{
		// p("Trying to connect to Zookeeper");
		// Socket s = new Socket(host, Integer.parseInt(port));
		// byte[] read = new byte[30];
		// s.getInputStream().read(read);
		// p("First 30 bytes read after opening db connection were: " + new
		// String(read));
		// }
		// catch(Exception ex){
		// ex.printStackTrace();
		// }

		//
		//
		// ApplicationContext appContext = new
		// ClassPathXmlApplicationContext("fwspring.xml");
		// IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
		// SendMail sendMail = (SendMail) appContext.getBean("sendMail");
		// IArchiveFactory archiveFactory = (IArchiveFactory)
		// appContext.getBean("archiveFactory");

	}
}
