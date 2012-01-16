package com.activequant.archive;

import java.io.IOException;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;

public interface IArchiveWriter {

    /**
     * Can be an expensive call, handle with care.
     * 
     * @throws IOException
     */
    public abstract void commit() throws IOException;

    public abstract void write(String instrumentId, TimeStamp timeStamp, Tuple<String, Double>... value)
            throws IOException;

    public abstract void write(String instrumentId, TimeStamp timeStamp, String[] keys, Double[] values);

    public abstract void write(String instrumentId, TimeStamp timeStamp, String key, Double value);

}