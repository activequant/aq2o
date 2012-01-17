package com.activequant.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.IArchiveWriter;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.utils.events.IEventListener;
import com.activequant.utils.worker.Worker;

/**
 * Imports recursively all ".csv" file (case sensitive) from a start folder.
 * Requires that the user specifies a market data provider name and a time
 * frame.
 * 
 * Date and time must be to separate columns.  
 * 
 * Date and time have to be in the format "yyyyMMdd", respectively  "HH:mm:ss.SSS". 
 * The file must have a header and must be comma
 * separated. Decimal separator must be a ".". Fields must only contain double
 * values. Filename of csv file is used as market data instrument provider
 * specific name.
 * 
 * 
 * @author ustaudinger
 * 
 */
public class ImportPriceSheet {

	private final ApplicationContext appContext;
	private final IDaoFactory idf;
	private final IMarketDataInstrumentDao mdiDao;
	private final IInstrumentDao idao;
	private LinkedBlockingQueue<String> fileNameQueue = new LinkedBlockingQueue<String>();
	private String mdProvider;
	private IArchiveFactory archiveFactory;
	private TimeFrame timeFrame;

	public ImportPriceSheet(String filename, final String mdprovider,
			String springInitFile, TimeFrame inTimeFrame) throws Exception {

		appContext = new ClassPathXmlApplicationContext(springInitFile);
		idf = (IDaoFactory) appContext.getBean("ibatisDao");
		mdiDao = idf.mdiDao();
		idao = idf.instrumentDao();
		archiveFactory = appContext.getBean("archiveFactory",
				IArchiveFactory.class);
		this.timeFrame = inTimeFrame;
		// will recurse into directory.
		this.mdProvider = mdprovider;
		new AnonymousWorker().process(filename);
	}

	class AnonymousWorker extends Worker<String> {
		private IArchiveWriter iaw = archiveFactory.getWriter(timeFrame);

		int lineCounter = 0;

		public void process(String event) {
			lineCounter = 0;
			if (event.equals("TERMINATE")) {
				setRunFlag(false);
				return;
			}
			System.out
					.println("********* Processing file name >" + event + "<");
			File f = new File(event);
			System.out.println("********* Created file. ");
			try {
				importFile(
						f.getAbsolutePath(), mdProvider);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				// rest sleep.
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void importFile(String fileName,	final String mdprovider) throws Exception {
			System.out.println("Importing " + fileName + " / " + mdprovider);
			final Date8Time6Parser d8t6p = new Date8Time6Parser();
			
			// load the excel file. 
			
			

			//
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyyMMdd HH:mm:ss.SSS");
			final SimpleDateFormat sdf2 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));


			final long l1 = System.currentTimeMillis();
			final CsvMapReader cmr = new CsvMapReader();
			cmr.read(new IEventListener<Map<String, String>>() {

				@Override
				public void eventFired(Map<String, String> event) {
					String date = event.get("DATE");
					
					// have to convert the date
					
					Long dl = Long.parseLong(date);
					long seconds = (dl-25569l)*86400l;
					Date d = new Date(seconds * 1000l);
					
					
					String time = "23:59:59.000";
					final String dateTime = sdf.format(d) + " " + time;
					final Iterator<Entry<String, String>> it = event.entrySet()
							.iterator();
					TimeStamp ts;
					try {
						if(dateTime.indexOf("-")!=-1)
							ts = new TimeStamp(sdf2.parse(dateTime));
						else
							ts = new TimeStamp(sdf.parse(dateTime));
						
						
						
						while (it.hasNext()) {
							Entry<String, String> entry = it.next();
							String key = entry.getKey().toUpperCase();
							if (key.equals("DATE"))
								continue;
							if (key.equals("TIME"))
								continue;

							MarketDataInstrument mdi = mdiDao.findByProvId(mdprovider, key);
							if(mdi==null)continue;
							if(entry.getValue().equals("#"))continue; 
							System.out.println("Importing : " + ts.getDate()+ " - " + key + " - " + entry.getValue());
							iaw.write(mdi.getId(), ts, "PX_SETTLE", 
									Double.parseDouble(entry
									.getValue()));
							if (lineCounter++ > 100) {
								lineCounter = 0;
								try {
									iaw.commit();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					} catch (ParseException e1) {
						e1.printStackTrace();
					} 
				}
			}, new FileInputStream(fileName));

			iaw.commit();
			final long l2 = System.currentTimeMillis();
			System.out.println("Took: " + (l2 - l1));

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String startFolder = args[0];
		String mdprovider = args[1];
		String springFile = args[2];
		String timeFrame = args[3];
		System.out.println("Importing from " + startFolder
				+ " all .csv files for " + mdprovider
				+ ". Using spring configuration " + springFile
				+ " and time frame " + timeFrame);
		new ImportPriceSheet(startFolder, mdprovider, springFile,
				TimeFrame.valueOf(timeFrame));

	}

}
