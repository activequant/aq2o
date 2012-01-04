package com.activequant.utils;

import java.io.FileInputStream;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.utils.events.IEventListener;

/**
 * Imports one CSV file that contains reference data, one instrument per row,
 * with multiple columns.
 * 
 * The file must have a header and must be comma separated.
 * 
 * 
 * @author ustaudinger
 * 
 */
public class ImportReferenceDataCSV {

	private final ApplicationContext appContext;
	private final IDaoFactory idf;
	private final IMarketDataInstrumentDao mdiDao;
	private final IInstrumentDao idao;

	public ImportReferenceDataCSV(String fileName, String springInitFile)
			throws Exception {

		appContext = new ClassPathXmlApplicationContext(springInitFile);
		idf = (IDaoFactory) appContext.getBean("ibatisDao");
		mdiDao = idf.mdiDao();
		idao = idf.instrumentDao();

		final CsvMapReader cmr = new CsvMapReader();
		cmr.read(new IEventListener<Map<String, String>>() {

			@Override
			public void eventFired(Map<String, String> event) {

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
		new ImportReferenceDataCSV(fileName, springFile);

	}

}
