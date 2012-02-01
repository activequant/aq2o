package com.activequant.utils;

import java.io.FileInputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.DaoException;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.domainmodel.Instrument;
import com.activequant.utils.events.IEventListener;

/**
 * Imports one CSV file that contains instrument reference data, one instrument per row,
 * with multiple columns.
 * 
 * The file must have a header and must be comma separated.
 * 
 * Each instrument must have at least: 
 * - CLASSNAME (this corresponds to the type)
 * - SHORTNAME
 * - NAME
 * - DESCRIPTION
 * - plus some class specific parameters, such as tick value, tick size. 
 * 
 * @author ustaudinger
 * 
 */
public class ImportInstrumentsCSV {

	private final ApplicationContext appContext;
	private final IDaoFactory idf;
	private final IInstrumentDao idao;
	private Logger log = Logger.getLogger(this.getClass());

	public ImportInstrumentsCSV(String fileName, String springInitFile)
			throws Exception {

		appContext = new ClassPathXmlApplicationContext(springInitFile);
		idf = (IDaoFactory) appContext.getBean("ibatisDao");
		idao = idf.instrumentDao();
		final InstanceFromMapInstantiator<Instrument> i = new InstanceFromMapInstantiator<Instrument>();
		
		final CsvMapReader cmr = new CsvMapReader();
		cmr.read(new IEventListener<Map<String, String>>() {
			@Override
			public void eventFired(Map<String, String> event) {
				Instrument instr = i.loadStringString(event);
				// 
				try {
					idao.update(instr);
					log.info("Created or updated instrument" + instr.getId());
				} catch (DaoException e) {
				    log.warn("Error while importing " + event, e);
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
		new ImportInstrumentsCSV(fileName, springFile);

	}

}
