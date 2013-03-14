package com.activequant.archive.basic;

import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.archive.TSContainer;
import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.archive.IArchiveReader;

/**
 * 
 * @author GhostRider
 *
 */
public class AQMSArchiveReader implements IArchiveReader {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MultiValueTimeSeriesIterator getMultiValueStream(String streamId,
			TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
