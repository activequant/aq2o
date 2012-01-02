package com.activequant.archive.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.activequant.archive.IArchiveReader;
import com.activequant.archive.TSContainer;
import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.utils.Date8Time6Parser;

/**
 * Reader class to read from HBase. All timestamps are in UTC and in Date8Time6
 * format. Due to performance reasons, the reader will not return date8time6
 * objects.
 * 
 * Visibility on package level only is intended! compare the corresponding
 * factory class.
 * 
 * @author ustaudinger
 * 
 */
class HBaseArchiveReader extends HBaseBase implements IArchiveReader {

    private Date8Time6Parser d8t6p = new Date8Time6Parser();

    HBaseArchiveReader(final String zookeeperHost, final TimeFrame tf) throws IOException {
        super(zookeeperHost, "TSDATA_" + tf.toString());
    }

    @Deprecated
    HBaseArchiveReader(final TimeFrame tf) throws IOException {
        super("TSDATA_" + tf.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.activequant.archive.IArchiveReader#getTimeSeries(java.lang.String,
     * java.lang.String, java.lang.Long)
     */
    public TSContainer getTimeSeries(final String instrumentId, final String value, final Date8Time6 startTimeStamp)
            throws Exception {
        return getTimeSeries(instrumentId, value, startTimeStamp, d8t6p.now());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.activequant.archive.IArchiveReader#getTimeSeries(java.lang.String,
     * java.lang.String, java.lang.Long, java.lang.Long)
     */
    public TSContainer getTimeSeries(final String instrumentId, final String value, final Date8Time6 startTimeStamp,
            final Date8Time6 stopTimeStamp) throws Exception {
        String startKey = instrumentId + "_" + startTimeStamp.asString();
        String stopKey = instrumentId + "_" + stopTimeStamp.asString();

        Scan s = new Scan(startKey.getBytes(), stopKey.getBytes());
        s.setMaxVersions(1);
        ResultScanner scanner = htable.getScanner(s);

        List<Double> timeStamps = new ArrayList<Double>();
        List<Double> values = new ArrayList<Double>();
        try {
            for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                if (rr.containsColumn("numbers".getBytes(), value.getBytes())) {
                    byte[] valB = rr.getValue("numbers".getBytes(), value.getBytes());
                    byte[] tsB = rr.getValue("numbers".getBytes(), "ts".getBytes());
                    Double val = Bytes.toDouble(valB);
                    Double ts = Bytes.toDouble(tsB);
                    timeStamps.add(ts);
                    values.add(val);
                }
            }
        } finally {
            scanner.close();
        }
        TSContainer ret = new TSContainer(timeStamps.toArray(new Double[] {}), values.toArray(new Double[] {}));
        return ret;
    }

}
