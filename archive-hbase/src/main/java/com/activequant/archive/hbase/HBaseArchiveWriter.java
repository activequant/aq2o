package com.activequant.archive.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveWriter;

/**
 * 
 * All timestamps are in UTC and in Nanoseconds. Flushing needs to be done
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

	HBaseArchiveWriter(final String zookeeperHost, final TimeFrame tf)
			throws IOException {
		super(zookeeperHost, 2181, "TSDATA_" + tf.toString());
	}

	HBaseArchiveWriter(final String zookeeperHost, final int zookeeperPort,
			final TimeFrame tf) throws IOException {
		super(zookeeperHost, zookeeperPort, "TSDATA_" + tf.toString());
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
	public void write(String seriesId, TimeStamp timeStamp,
			Tuple<String, Double>... value) throws IOException {

		for (Tuple<String, Double> t : value) {
			write(seriesId, timeStamp, t.getA(), t.getB());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.activequant.archive.IArchiveWriter#write(java.lang.String,
	 * java.lang.Long, java.lang.String[], java.lang.Double[])
	 */
	public void write(String seriesId, TimeStamp timeStamp, String[] keys,
			Double[] values) {
		assert (values != null);
		assert (keys != null);
		assert (keys.length == values.length);

		for (int i = 0; i < keys.length; i++) {
			write(seriesId, timeStamp, keys[i], values[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.activequant.archive.IArchiveWriter#write(java.lang.String,
	 * java.lang.Long, java.lang.String, java.lang.Double)
	 */
	public void write(String seriesId, TimeStamp timeStamp, String key,
			Double value) {
		assert (key != null);
		assert (value != null);
		String rowKey = seriesId + "_" + padded(timeStamp.toString());
		Put p = new Put(rowKey.getBytes());
		// 
		p.add("numbers".getBytes(), key.getBytes(), Bytes.toBytes(value));
		p.add("numbers".getBytes(), "ts".getBytes(),
				Bytes.toBytes(timeStamp.getNanoseconds()));
		
		// also add the key to the series/key map. 
		Put p2 = new Put(("FIELDS_"+seriesId).getBytes());
		p2.add("fields".getBytes(), key.getBytes(), Bytes.toBytes(1));
		
		
		// relatively expensive call. have to find a way around.
		// maybe by using a special class.
		synchronized (puts) {
			puts.add(p);
			puts.add(p2);
		}

	}

	@Override
	public void delete(String seriesKey, String valueKey) throws IOException {
		delete(seriesKey, valueKey, new TimeStamp(0L), new TimeStamp(Long.MAX_VALUE));
	}

	@Override
	public void delete(String seriesKey, String valueKey, TimeStamp from,
			TimeStamp to) throws IOException {
		ResultScanner scanner = null;
		try {
			scanner = getScanner(seriesKey, from, to);
			List<Delete> rows = new ArrayList<Delete>();
			for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
				if (rr.containsColumn("numbers".getBytes(), valueKey.getBytes())) {
                    Delete del = new Delete(rr.getRow());
                    del.deleteColumn("numbers".getBytes(), valueKey.getBytes());
					rows.add(del);
                }
			}
			htable.delete(rows);
		} finally {
			if (scanner != null)
				scanner.close();
		}
	}

	@Override
	public void delete(String seriesKey) throws IOException {
		delete(seriesKey, new TimeStamp(0L), new TimeStamp(Long.MAX_VALUE));

	}

	@Override
	public void delete(String seriesKey, TimeStamp from, TimeStamp to)
			throws IOException {
		ResultScanner scanner = null;
		try {
			scanner = getScanner(seriesKey, from, to);
			List<Delete> rows = new ArrayList<Delete>();
			for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
				rows.add(new Delete(rr.getRow()));
			}
			htable.delete(rows);
		} finally {
			if (scanner != null)
				scanner.close();
		}
	}
	

	@Override
	public void close() throws IOException {
		this.htable.close();
	}


}
