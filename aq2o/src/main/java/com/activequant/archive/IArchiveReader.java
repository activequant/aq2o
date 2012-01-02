package com.activequant.archive;

import com.activequant.domainmodel.Date8Time6;

public interface IArchiveReader {

    public abstract TSContainer getTimeSeries(String instrumentId, String key, Date8Time6 startTimeStamp)
            throws Exception;

    public abstract TSContainer getTimeSeries(String instrumentId, String key, Date8Time6 startTimeStamp,
            Date8Time6 stopTimeStamp) throws Exception;

}