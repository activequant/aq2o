package com.activequant.archive.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.activequant.archive.IArchiveWriter;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;

/**
 * 
 * All timestamps are in UTC and in Milliseconds. Flushing needs to be done
 * manually - no flushing will be done by the archive writer, which means that
 * all values stay in memory until flushed! Data loss is at your risk!
 * 
 * Visibility on package level only is intended! compare the corresponding
 * factory class.
 * 
 * @author ustaudinger
 * 
 */
class HBaseArchiveWriter extends HBaseBase implements IArchiveWriter {

    private List<Put> puts = new ArrayList<Put>();

    HBaseArchiveWriter(final String zookeeperHost, final TimeFrame tf) throws IOException {
        super(zookeeperHost, "TSDATA_" + tf.toString());
    }

    @Deprecated
    HBaseArchiveWriter(final TimeFrame tf) throws IOException {
        super("TSDATA_" + tf.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.activequant.archive.IArchiveWriter#commit()
     */
    public void commit() throws IOException {

        synchronized (puts) {
            htable.put(puts);
            puts.clear();
            htable.flushCommits();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.activequant.archive.IArchiveWriter#write(java.lang.String,
     * java.lang.Long, com.activequant.domainmodel.Tuple)
     */
    public void write(String instrumentId, TimeStamp timeStamp, Tuple<String, Double>... value) throws IOException {

        for (Tuple<String, Double> t : value) {
            write(instrumentId, timeStamp, t.getA(), t.getB());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.activequant.archive.IArchiveWriter#write(java.lang.String,
     * java.lang.Long, java.lang.String[], java.lang.Double[])
     */
    public void write(String instrumentId, TimeStamp timeStamp, String[] keys, Double[] values) {
        assert (values != null);
        assert (keys != null);
        assert (keys.length == values.length);

        for (int i = 0; i < keys.length; i++) {
            write(instrumentId, timeStamp, keys[i], values[i]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.activequant.archive.IArchiveWriter#write(java.lang.String,
     * java.lang.Long, java.lang.String, java.lang.Double)
     */
    public void write(String instrumentId, TimeStamp timeStamp, String key, Double value) {
        assert (key != null);
        assert (value != null);
        String rowKey = instrumentId + "_" + timeStamp.toString();
        Put p = new Put(rowKey.getBytes());
        p.add("numbers".getBytes(), key.getBytes(), Bytes.toBytes(value));
        p.add("numbers".getBytes(), "ts".getBytes(), Bytes.toBytes(timeStamp.getNanoseconds()));
        // relatively expensive call. have to find a way around.
        // maybe by using a special class.
        synchronized (puts) {
            puts.add(p);
        }

    }

}
