package com.activequant.backtesting;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveReader;
import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.IMarketDataInstrumentDao;

public class Backtester {
	
	private String[] marketDataInstrumentIds; 
	private IArchiveReader archiveReader; 
	private IMarketDataInstrumentDao mdiDao; 
	private IInstrumentDao instrumentDao; 
	
	public void init()
	{
		// load 
		
	}
	
	public void execute()
	{
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		String initFile = "backtester.xml";
		if(args.length>0)initFile = args[0];
		ApplicationContext appContext = new ClassPathXmlApplicationContext(initFile);
		Backtester bt = appContext.getBean("backtester", Backtester.class);
		bt.init();
		bt.execute();
	}

}
