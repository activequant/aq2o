package com.activequant.utils;

import java.io.FileInputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.ITradeableInstrumentDao;
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
public class ImportTradeableInstrumentsCSV {

	private final ApplicationContext appContext;
	private final IDaoFactory idf;
	private final ITradeableInstrumentDao tdiDao;
	private final Logger log = Logger.getLogger(this.getClass());

	public ImportTradeableInstrumentsCSV(String fileName, String springInitFile)
			throws Exception {

		appContext = new ClassPathXmlApplicationContext(springInitFile);
		idf = (IDaoFactory) appContext.getBean("ibatisDao");
		tdiDao = idf.tradeableDao();
		final InstanceFromMapInstantiator<TradeableInstrument> i = new InstanceFromMapInstantiator<TradeableInstrument>();
		
		final CsvMapReader cmr = new CsvMapReader();
		cmr.read(new IEventListener<Map<String, String>>() {
			@Override
			public void eventFired(Map<String, String> event) {
				TradeableInstrument instr = i.loadStringString(event);
				// 
				try {
					tdiDao.update(instr);
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
		new ImportTradeableInstrumentsCSV(fileName, springFile);

	}

}
