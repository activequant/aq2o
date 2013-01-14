package com.activequant.server.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.transport.ITransportFactory;

/**
 * sample component that downloads data from quandl and stores it into the
 * archive.
 * 
 * @author GhostRider
 * 
 */
public class QuandlDownloaderComponent extends ComponentBase {

	public QuandlDownloaderComponent(ITransportFactory transFac, final IArchiveFactory archFactory)
			throws Exception {
		super("YahooDownloader", transFac);

		Runnable r = new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000 * 60);
						// check if it is a new day.

						File f = new File("quandl_symbols.csv");
						if (f.exists()) {
							//
							BufferedReader br = new BufferedReader(
									new FileReader(f));
							String l = br.readLine();
							while (l != null) {
								// http://www.quandl.com/api/v1/datasets/OFDP/ALUMINIUM_21.csv?
								String url = "http://www.quandl.com/api/v1/datasets/"
										+ l + ".csv?";
								URL u = new URL(url);
								BufferedReader br2 = new BufferedReader(
										new InputStreamReader(u.openStream()));

								String inputLine;
								StringBuffer sb = new StringBuffer();
								while ((inputLine = br2.readLine()) != null) {
									sb.append(inputLine).append("\n");
								}
								br2.close(); 
								// now that we have all data in our string buffer, let's parse it. 
								archFactory.getWriter(TimeFrame.EOD);
								// have to parse, have to write it ... 
								// trivial three lines. up to you. 
								// 
								
								
								l = br.readLine();
							}

						}
						//
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	@Override
	public String getDescription() {
		//
		return "The Yahoo Downloader component downloads daily data from Yahoo. ";

	}

}
