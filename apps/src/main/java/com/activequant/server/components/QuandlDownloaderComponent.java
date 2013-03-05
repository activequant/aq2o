package com.activequant.server.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveWriter;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.server.components.ReplicatorSlaveComponent.ReplicationTask;
import com.activequant.transport.activemq.ActiveMQTransportFactory;

/**
 * sample component that downloads data from quandl and stores it into the
 * archive.
 * 
 * @author GhostRider
 * 
 */
public class QuandlDownloaderComponent extends ComponentBase {
	private final IArchiveFactory a;
	private final IDaoFactory d;
	private static boolean running = false; 

	public static void main(String[] args) throws Exception {

		ITransportFactory t = new ActiveMQTransportFactory("localhost", 61616);
		IArchiveFactory a = new HBaseArchiveFactory("localhost");
		IDaoFactory d = null;
		new QuandlDownloaderComponent(t, a, d);
	}

	private MarketDataInstrument initQuandlMdi(String mdiId) throws DaoException {
		MarketDataInstrument mdi = new MarketDataInstrument();
		mdi.setMdProvider("QUANDL");
		mdi.setProviderSpecificId(mdiId);
		d.mdiDao().update(mdi);
		return mdi;
	}

	class DownloadTask extends TimerTask {
		public void run() {
			log.info("Started a download task."); 
			if(running)
			{
				log.info("There is already one download task running, not starting another."); 
				return;
			}
			running = true; 
			try {
				//
				// Runnable r = new Runnable() {
				// public void run() {
				// while (true) {
				// try {
				// Thread.sleep(1000 * 60);
				// check if it is a new day.

				File f = new File("quandl_symbols.csv");
				if (f.exists()) {
					log.info("Symbols file exists.");
					//
					BufferedReader br = new BufferedReader(new FileReader(f));
					String l = br.readLine();
					while (l != null) {
						// 
						log.info("Fetching " + l);
						// let's see if we have a quandl mdi already.
						MarketDataInstrument mdi = initQuandlMdi(l);
						
						// example:
						// http://www.quandl.com/api/v1/datasets/OFDP/ALUMINIUM_21.csv?
						// http://www.quandl.com/api/v1/datasets/IMF/POILWTI_USD.csv?&auth_token=sR5ozVJPXc8drGTdra1C&trim_start=1980-01-31&trim_end=2013-01-31&sort_order=desc
						String url = "http://www.quandl.com/api/v1/datasets/"
								+ l + ".csv?&auth_token=sR5ozVJPXc8drGTdra1C&";
						URL u = new URL(url);
						BufferedReader br2 = new BufferedReader(
								new InputStreamReader(u.openStream()));

						IArchiveWriter iaw = a.getWriter(TimeFrame.EOD);
						String inputLine;
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						List<String> headers = new ArrayList<String>();
						int counter = 0;
						int lineCount = 0;
						while ((inputLine = br2.readLine()) != null) {
							System.out.println(inputLine);

							counter++;
							if (counter > 100) {
								counter = 0;
								iaw.commit();
							}
							if (inputLine != null && lineCount == 0) {
								//
								String[] h = inputLine.split(",");
								for (int i = 0; i < h.length; i++) {
									if (i > 0) {
										headers.add(h[i]);
									}
								}
								//
							} else if (inputLine != null
									&& !inputLine.startsWith("Date")) {
								String[] parts = inputLine.split(",");
								TimeStamp ts = new TimeStamp(
										sdf.parse(parts[0]));
								for (int i = 0; i < headers.size(); i++) {
									Double val = Double
											.parseDouble(parts[i + 1]);
									iaw.write(mdi.getId(), ts, headers.get(i)
											.toUpperCase(), val);
								}
								//
							}
							lineCount++;

						}
						iaw.commit();
						br2.close();
						log.info("Fetched " + l+".") ;
						// now that we have all data in our string buffer, let's
						// parse
						// it.

						// have to parse, have to write it ...
						// trivial three lines. up to you.
						//
						l = br.readLine();
					}

				}
				else
					log.info("quandl_symbols.csv does not exist."); 


			} catch (Exception ex) {
				log.warn("Download failed : ", ex); 
			}
			running = false; 
		}
	}

	public QuandlDownloaderComponent(ITransportFactory transFac,
			final IArchiveFactory archFactory, final IDaoFactory daoFactory)
			throws Exception {
		super("QuandlDownloader", transFac);
		this.d = daoFactory;
		this.a = archFactory;

		//
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		// }
		// }
		// };
		// Thread t = new Thread(r);
		// t.start();

		Timer timer = new Timer();
		timer.schedule(new DownloadTask(), 5 * 60 * 1000, 6 * 60  * 60 * 1000);

	}
	
	
	public void customMessage(String message) {
		log.info("Message: " + message); 
		if (message.equals("REFETCH")) {
			Thread t = new Thread(new DownloadTask());
			t.start(); 
		}
	}
	

	@Override
	public String getDescription() {
		//
		return "The Quandl Downloader component downloads data from Quandl. It is running: "+ running;

	}

}
