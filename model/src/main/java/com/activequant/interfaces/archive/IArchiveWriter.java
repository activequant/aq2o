package com.activequant.interfaces.archive;

import java.io.IOException;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;

/**
 * 
 * This interface collects archive writer related functionality. 
 * 
 * 
 * @author GhostRider
 *
 */
public interface IArchiveWriter {

    /**
     * Can be an expensive call, handle with care.
     * All client code must call commit() to signal that data is complete and should be written
     * to the underlying storage mechanism. 
     * 
     * @throws IOException
     */
    public void commit() throws IOException;

    
    public void write(String instrumentId, TimeStamp timeStamp, Tuple<String, Double>... value)
            throws IOException;

    public void write(String instrumentId, TimeStamp timeStamp, String[] keys, Double[] values);

    public void write(String instrumentId, TimeStamp timeStamp, String key, Double value);

    public void delete(String seriesKey, String valueKey) throws IOException;
    
    public void delete(String seriesKey, String valueKey, TimeStamp from, TimeStamp to) throws IOException;
    
    /**
     * deletes all data for this writer and this serieskey. 
     * 
     * @param seriesKey
     * @throws IOException
     */
    public void delete(String seriesKey) throws IOException;
    
    public void delete(String seriesKey, TimeStamp from, TimeStamp to) throws IOException;
    
    public void close() throws IOException; 
}