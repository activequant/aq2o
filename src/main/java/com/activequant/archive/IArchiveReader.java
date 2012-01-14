package com.activequant.archive;

import com.activequant.domainmodel.Date8Time6;

public interface IArchiveReader {

	public TSContainer getTimeSeries(String streamId, String key,
			Date8Time6 startTimeStamp) throws Exception;

	public TSContainer getTimeSeries(String streamId, String key,
			Date8Time6 startTimeStamp, Date8Time6 stopTimeStamp)
			throws Exception;

	public TimeSeriesIterator getTimeSeriesStream(String streamId, String key,
			Date8Time6 startTimeStamp, Date8Time6 stopTimeStamp)
			throws Exception;
}