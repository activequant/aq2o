package com.activequant.archive.basic;

import java.io.IOException;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveWriter;

/**
 * Will buffer all data until COMMIT is called. 
 * 
 * @author GhostRider
 *
 */
public class AQMSArchiveWriter implements IArchiveWriter{

	@Override
	public void commit() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(String instrumentId, TimeStamp timeStamp,
			Tuple<String, Double>... value) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(String instrumentId, TimeStamp timeStamp, String[] keys,
			Double[] values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(String instrumentId, TimeStamp timeStamp, String key,
			Double value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String seriesKey, String valueKey) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String seriesKey, String valueKey, TimeStamp from,
			TimeStamp to) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String seriesKey) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String seriesKey, TimeStamp from, TimeStamp to)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

}
