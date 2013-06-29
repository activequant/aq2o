package com.activequant.archive.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.archive.TSContainer;
import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveReader;

/**
 * 
 * @author GhostRider
 * 
 */
public class AQMSArchiveReader implements IArchiveReader {

	private TimeFrame timeFrame;
	private String baseUrl;
	private Logger log = Logger.getLogger(AQMSArchiveReader.class);

	public AQMSArchiveReader(String baseUrl, TimeFrame timeFrame) {
		this.timeFrame = timeFrame;
		this.baseUrl = baseUrl; 
	}

	@Override
	public TSContainer getTimeSeries(String streamId, String key,
			TimeStamp startTimeStamp) throws Exception {
		return null;
	}

	@Override
	public TSContainer getTimeSeries(String streamId, String key,
			TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeSeriesIterator getTimeSeriesStream(String streamId, String key,
			TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception {
		return null;
	}

	/**
	 * dirty hack, just takes open, high,low,close for now. Need to think about a
	 * way to link AQMS and this piece together. Maybe through some additional parameter
	 * that tells AQMS to always include field names for every row ... or something like that. 
	 * 
	 */
	@Override
	public MultiValueTimeSeriesIterator getMultiValueStream(String streamId,
			TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String url = baseUrl + "?SERIESID="+streamId+"&FREQ=" + timeFrame.toString()+"&FIELD=OPEN,HIGH,LOW,CLOSE&STARTDATE="+sdf.format(startTimeStamp.getCalendar().getTime())+"&ENDDATE="+sdf.format(stopTimeStamp.getCalendar().getTime());
		log.info("Using url: " + url);
		// let's read from this url ... 
		URL u = new URL(url);
		InputStream inStream = u.openStream();
		final BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		// skip the header. 
		br.readLine();
		class LocalIter extends MultiValueTimeSeriesIterator{
			String nextLine = null;
			@Override
			public boolean hasNext() {
				return nextLine!=null;
			}
			@Override
			public Tuple<TimeStamp, Map<String, Double>> next() {
				if(nextLine!=null){
					
					// let's parse the line. 
					String[] s = nextLine.split(",");
					// 
					Double open = s[2]==null?Double.NaN:Double.parseDouble(s[2]);
					Double high = s[3]==null?Double.NaN:Double.parseDouble(s[3]);
					Double low  = s[4]==null?Double.NaN:Double.parseDouble(s[4]);
					Double close = s[5]==null?Double.NaN:Double.parseDouble(s[5]);
					// 
					TimeStamp ts = new TimeStamp(Long.parseLong(s[0]));
					Map<String,Double> m = new HashMap<String, Double>();
					m.put("OPEN", open);
					m.put("HIGH", high);
					m.put("LOW", low);
					m.put("CLOSE", close);
					Tuple<TimeStamp, Map<String, Double>> t = new Tuple<TimeStamp, Map<String, Double>>();
					t.setA(ts);
					t.setB(m);
					// 
					try {
						nextLine = br.readLine();
					} catch (IOException e) {
						nextLine = null; 
						e.printStackTrace();
					} 
					return t; 

				}
				return null;
			}		
			
		};
		// 
		LocalIter li = new LocalIter(); 
		
		// read the first line. 
		final String firstDataLine = br.readLine(); 
		li.nextLine = firstDataLine; 
		
		return li;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
