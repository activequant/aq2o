package com.activequant.archive.csv;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.archive.TSContainer;
import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.utils.CsvMapReader;
import com.activequant.utils.events.IEventListener;

/**
 * Can handle only single-stream CSV files. Not multiple instruments in one
 * file.
 * 
 * @author ustaudinger
 * 
 */
public class CsvArchiveReaderFormat1 implements IArchiveReader {

	private String fileName;

	public CsvArchiveReaderFormat1(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public TSContainer getTimeSeries(final String streamId, final String key, final TimeStamp startTimeStamp)
			throws Exception {
		final List<TimeStamp> timeStamps = new ArrayList<TimeStamp>();
		final List<Double> values = new ArrayList<Double>();
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		// import the soybeans example and treat it like a performance curve.
		new CsvMapReader().read(new IEventListener<Map<String, String>>() {
			@Override
			public void eventFired(Map<String, String> event) {
				try {
					String date = event.get("DATE");
					String value = event.get(key);
					TimeStamp ts = new TimeStamp(sdf.parse(date));
					if (ts.isAfter(startTimeStamp)) {
						timeStamps.add(ts);
						values.add(Double.parseDouble(value));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}, new FileInputStream(fileName));
		return new TSContainer(timeStamps.toArray(new TimeStamp[] {}), values.toArray(new Double[] {}));
	}

	@Override
	public TSContainer getTimeSeries(String streamId, final String key, final TimeStamp startTimeStamp,
			final TimeStamp stopTimeStamp) throws Exception {
		final List<TimeStamp> timeStamps = new ArrayList<TimeStamp>();
		final List<Double> values = new ArrayList<Double>();
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		// import the soybeans example and treat it like a performance curve.
		new CsvMapReader().read(new IEventListener<Map<String, String>>() {
			@Override
			public void eventFired(Map<String, String> event) {
				try {
					String date = event.get("DATE");
					String value = event.get(key);
					TimeStamp ts = new TimeStamp(sdf.parse(date));
					if (ts.isAfter(startTimeStamp) && ts.isBefore(stopTimeStamp)) {
						timeStamps.add(ts);
						values.add(Double.parseDouble(value));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}, new FileInputStream(fileName));
		return new TSContainer(timeStamps.toArray(new TimeStamp[] {}), values.toArray(new Double[] {}));
	}

	@Override
	public TimeSeriesIterator getTimeSeriesStream(String streamId, String key, TimeStamp startTimeStamp,
			TimeStamp stopTimeStamp) throws Exception {
		// make an iterator.

		final TSContainer t = getTimeSeries(streamId, key, startTimeStamp, stopTimeStamp);
		
		TimeSeriesIterator tsi = new TimeSeriesIterator() {
			int index = 0; 
			@Override
			public Tuple<TimeStamp, Double> next() {
				if(index!=t.values.length){
					Tuple<TimeStamp, Double> ret = new Tuple<TimeStamp, Double>(t.timeStamps[index], t.values[index]);
					index = index + 1;
					return ret;
				}
				return null;
			}			
			@Override
			public boolean hasNext() {
				if(index<t.values.length)return true; 
				return false; 
			}
		};
		
		return tsi; 
	}

	@Override
	public MultiValueTimeSeriesIterator getMultiValueStream(String streamId, TimeStamp startTimeStamp,
			TimeStamp stopTimeStamp) throws Exception {
		return null;
	}

}
