package com.activequant.interfaces.archive;

import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.archive.TSContainer;
import com.activequant.archive.TimeSeriesIterator;
import com.activequant.domainmodel.TimeStamp;

/**
 * 
 * @author GhostRider
 *
 */
public interface IArchiveReader {

    public TSContainer getTimeSeries(String streamId, String key, TimeStamp startTimeStamp) throws Exception;

    public TSContainer getTimeSeries(String streamId, String key, TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception;

    public TimeSeriesIterator getTimeSeriesStream(String streamId, String key, TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception;

    public MultiValueTimeSeriesIterator getMultiValueStream(String streamId, TimeStamp startTimeStamp, TimeStamp stopTimeStamp) throws Exception;
}