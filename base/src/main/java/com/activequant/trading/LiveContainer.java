package com.activequant.trading;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.trading.ITradingSystem;
import com.activequant.interfaces.transport.ITransportFactory;

/**
 * stripped down live container. Work-in-progress
 * 
 * @author GhostRider
 *
 */
public class LiveContainer {

	public LiveContainer(String springFile, ITradingSystem tradSys) throws Exception {
		//
		ApplicationContext appContext = new ClassPathXmlApplicationContext(springFile);
		IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");		
		//
		// initialize transport layer and VirtEX
		ITransportFactory transport = (ITransportFactory) appContext.getBean("jmsTransport");
		// 		
		TradingSystemEnvironment tradSysEnv = new TradingSystemEnvironment();
		tradSysEnv.setArchiveFactory(null);
		tradSysEnv.setDaoFactory(idf);
		tradSysEnv.setTransportFactory(transport);
		// manage the trading system  
		if(tradSys!=null){
			tradSys.environment(tradSysEnv);
			tradSys.initialize(); 
			tradSys.start();
		}
		
	}

	public static void main(String[] args) throws Exception {
		new LiveContainer("fwspring.xml", null);
	}
}
