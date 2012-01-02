package com.activequant.archive;

import java.io.IOException;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.Tuple;

public interface IArchiveWriter {

    /**
     * Can be an expensive call, handle with care.
     * 
     * @throws IOException
     */
    public abstract void commit() throws IOException;

    public abstract void write(String instrumentId, Date8Time6 timeStamp, Tuple<String, Double>... value)
            throws IOException;

    public abstract void write(String instrumentId, Date8Time6 timeStamp, String[] keys, Double[] values);

    public abstract void write(String instrumentId, Date8Time6 timeStamp, String key, Double value);

}