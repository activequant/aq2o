package com.activequant.archive;

import com.activequant.domainmodel.TimeStamp;

public interface IArchiveReader {

    public TSContainer getTimeSeries(String streamId, String key, TimeStamp startTimeStamp) throws Exception;

    public TSContainer getTimeSeries(String streamId, String key, TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception;

    public TimeSeriesIterator getTimeSeriesStream(String streamId, String key, TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception;

    public MultiValueTimeSeriesIterator getMultiValueStream(String streamId, TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception;
}