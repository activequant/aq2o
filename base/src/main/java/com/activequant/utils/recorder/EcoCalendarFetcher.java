package com.activequant.utils.recorder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class EcoCalendarFetcher {

	public EcoCalendarFetcher() throws IOException, URISyntaxException {

		// get the most recent sunday.
		Calendar cal = GregorianCalendar.getInstance();
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DAY_OF_WEEK, -(dow - 1));
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

		// http://www.dailyfx.com/files/Calendar-02-17-2013.csv
		DefaultHttpClient client = new DefaultHttpClient();
		URL u = new URL("http://www.dailyfx.com/files/Calendar-"
				+ sdf.format(cal.getTime()) + ".csv");
		System.out.println(u.toString());
		HttpGet request = new HttpGet(u.toURI());
		HttpResponse response = client.execute(request);

		BufferedReader br = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String l = br.readLine();
		// l has our header. 
		if(l.equals("Date,Time,Time Zone,Currency,Event,Importance,Actual,Forecast,Previous")){
			l = br.readLine();
			while(l!=null){
				if(l.indexOf("Wed Feb 20,13:30")!=-1){
					System.out.println(l);
				}
				
				String[] parts = l.split(",");
				// 
				l = br.readLine();
			}
		
		}
		
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws IOException,
			URISyntaxException {

		new EcoCalendarFetcher();

	}

}
