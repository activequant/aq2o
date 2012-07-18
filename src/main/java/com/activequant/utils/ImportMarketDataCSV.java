package com.activequant.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveWriter;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.IInstrumentDao;
import com.activequant.interfaces.dao.IMarketDataInstrumentDao;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.utils.worker.Worker;
import com.activequant.utils.worker.WorkerThread;

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
public class ImportMarketDataCSV {

	private final ApplicationContext appContext;
	private final IDaoFactory idf;
	private final IMarketDataInstrumentDao mdiDao;
	private final IInstrumentDao idao;
	private LinkedBlockingQueue<String> fileNameQueue = new LinkedBlockingQueue<String>();
	private String mdProvider;
	private IArchiveFactory archiveFactory;
	private TimeFrame timeFrame;
	private boolean rewriteFilenameForBB = true; 

	public ImportMarketDataCSV(String directory, final String mdprovider,
			String springInitFile, TimeFrame inTimeFrame) throws Exception {

		appContext = new ClassPathXmlApplicationContext(springInitFile);
		idf = (IDaoFactory) appContext.getBean("ibatisDao");
		mdiDao = idf.mdiDao();
		idao = idf.instrumentDao();
		archiveFactory = appContext.getBean("archiveFactory",
				IArchiveFactory.class);
		this.timeFrame = inTimeFrame;
		// will recurse into directory.
		File f = new File(directory);
		System.out.println("Traversing directory " + f.getAbsolutePath());
		this.mdProvider = mdprovider;
		new FileTraversal() {
			public void onFile(final File f) {
				System.out.println(f);
				if (f.getName().toLowerCase().endsWith(".csv")) {
					// parse it.
					try {
						fileNameQueue.add(f.getAbsolutePath());
						// new AnonymousWorker().process(f.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.traverse(f);

		for (int i = 0; i < 100; i++) {
			fileNameQueue.add("TERMINATE");
		}

		//
		new Thread(new WorkerThread<String>(fileNameQueue,
				new AnonymousWorker())).start();
		new Thread(new WorkerThread<String>(fileNameQueue,
				new AnonymousWorker())).start();
		new Thread(new WorkerThread<String>(fileNameQueue,
				new AnonymousWorker())).start();
		new Thread(new WorkerThread<String>(fileNameQueue,
				new AnonymousWorker())).start();

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
						f.getAbsolutePath(),
						f.getName()
								.substring(
										0,
										f.getName().toLowerCase()
												.indexOf(".csv")).trim(),
						mdProvider);
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

		public void importFile(String fileName, String providerspecificid,
				String mdprovider) throws Exception {
			System.out.println("Importing " + fileName + " / " + mdprovider
					+ " / " + providerspecificid);
			final Date8Time6Parser d8t6p = new Date8Time6Parser();
			MarketDataInstrument tempMdi = mdiDao.findByProvId(mdprovider,
					providerspecificid);
			if (tempMdi == null) {
				tempMdi = new MarketDataInstrument();
				tempMdi.setProviderSpecificId(providerspecificid);
				tempMdi.setMdProvider(mdprovider);
				mdiDao.create(tempMdi);
			}
			final MarketDataInstrument mdi = tempMdi;
			tempMdi = null;

			//
			final SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyyMMdd HH:mm:ss.SSS");
			final SimpleDateFormat sdf2 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.SSS");
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));

			System.out.println("All set, ready to parse for " + mdi.getId()
					+ " /// " + System.currentTimeMillis());

			final long l1 = System.currentTimeMillis();
			final CsvMapReader cmr = new CsvMapReader();
			cmr.read(new IEventListener<Map<String, String>>() {

				@Override
				public void eventFired(Map<String, String> event) {
					final String date = event.get("DATE");
					String time = event.get("TIME");
					if(time==null)time = "00:00:00.000";
					final String dateTime = date + " " + time;
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

							iaw.write(mdi.getId(), ts, key, 
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
		new ImportMarketDataCSV(startFolder, mdprovider, springFile,
				TimeFrame.valueOf(timeFrame));

	}

}
