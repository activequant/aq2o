package com.activequant.utils;

import java.io.FileInputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.IMarketDataInstrumentDao;
import com.activequant.interfaces.utils.IEventListener;

/**
 * Imports one CSV file that contains instrument reference data, one instrument per row,
 * with multiple columns.
 * 
 * The file must have a header and must be comma separated.
 * 
 * The following fields MUST exist: 
 * className, instrumentId, mdProvider, providerSpecificId
 * 
 * examples are present in the resources folder. 
 * 
 * @author ustaudinger
 * 
 */
public class ImportMarketDataInstrumentsCSV {

	private final ApplicationContext appContext;
	private final IDaoFactory idf;
	private final IMarketDataInstrumentDao mdiDao;
	private final Logger log = Logger.getLogger(this.getClass());

	public ImportMarketDataInstrumentsCSV(String fileName, String springInitFile)
			throws Exception {

		appContext = new ClassPathXmlApplicationContext(springInitFile);
		idf = (IDaoFactory) appContext.getBean("ibatisDao");
		mdiDao = idf.mdiDao();
		final InstanceFromMapInstantiator<MarketDataInstrument> i = new InstanceFromMapInstantiator<MarketDataInstrument>();
		
		final CsvMapReader cmr = new CsvMapReader();
		cmr.read(new IEventListener<Map<String, String>>() {
			@Override
			public void eventFired(Map<String, String> event) {
				MarketDataInstrument instr = i.loadStringString(event);
				// 
				try {
					mdiDao.update(instr);
					log.info("Updated or created instrument: " + instr.getId());
				} catch (DaoException e) {
					System.err.println("Error while importing " + event);
					e.printStackTrace();
				}
				
			}
		}, new FileInputStream(fileName));

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String fileName = args[0];
		String springFile = args[1];
		System.out.println("Importing from " + fileName
				+ ". Using spring configuration " + springFile);
		new ImportMarketDataInstrumentsCSV(fileName, springFile);

	}

}
