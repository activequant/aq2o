package com.activequant.dao.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.activequant.dao.DaoException;

public class TagDao {
	private List<Put> puts = new ArrayList<Put>();
	protected HTable htable;
	private final Logger log = Logger.getLogger(TagDao.class);

	public TagDao(final String zookeeperQuorumHost) throws IOException {
		this(zookeeperQuorumHost, 2181, "TAGS");
	}

	public TagDao(final String zookeeperQuorumHost, final int zookeeperPort, final String tableName) throws IOException {
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", zookeeperQuorumHost + ":" + zookeeperPort);

		HBaseAdmin admin = new HBaseAdmin(config);
		if (!admin.tableExists(tableName.getBytes())) {
			log.info("HTable for Tags doesn't exist. Creating it.");
			// create the table.
			HTableDescriptor desc = new HTableDescriptor(tableName.getBytes());
			HColumnDescriptor col1 = new HColumnDescriptor("tags".getBytes());
			desc.addFamily(col1);
			admin.createTable(desc);
			boolean avail = admin.isTableAvailable(tableName.getBytes());
			log.info("HTable for Tags is available: " + avail);
		}
		htable = new HTable(config, tableName.getBytes());
		htable.setAutoFlush(false);
		htable.setScannerCaching(1000000);
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

	public void tag(String objectType, String objectId, String tag) {
		String rowKey = objectType + "_" + objectId;
		Put p = new Put(rowKey.getBytes());
		p.add("tags".getBytes(), tag.toUpperCase().getBytes(), Bytes.toBytes(1));
		p.add("tags".getBytes(), "OBJECTID".getBytes(), Bytes.toBytes(objectId));
		p.add("tags".getBytes(), "OBJECTTYPE".getBytes(), Bytes.toBytes(objectType));
		// relatively expensive call. have to find a way around.
		// maybe by using a special class.
		synchronized (puts) {
			puts.add(p);
		}

	}

	public void untag(String objectType, String objectId, String tag) {
		String rowKey = objectType + "_" + objectId;
		Put p = new Put(rowKey.getBytes());
		p.add("tags".getBytes(), tag.toUpperCase().getBytes(), Bytes.toBytes(0));
		synchronized (puts) {
			puts.add(p);
		}
	}

	public String[] getAll() throws DaoException {
		try {
			List<String> ret = new ArrayList<String>();
			Scan s = new Scan();
			s.setMaxVersions(1);
			ResultScanner scanner = htable.getScanner(s);
			try {
				for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
					String localObjectId = new String(rr.getValue("tags".getBytes(), "OBJECTID".getBytes()));
					ret.add(localObjectId);

				}
			} finally {
				scanner.close();
			}

			return ret.toArray(new String[] {});
		} catch (IOException ex) {
			throw new DaoException(ex);
		}

	}

	public String[] getTags(String objectType, String objectId) throws IOException {
		List<String> ret = new ArrayList<String>();
		String rowKey = objectType + "_" + objectId;
		Scan s = new Scan(rowKey.getBytes(), rowKey.getBytes());
		s.setMaxVersions(1);
		ResultScanner scanner = htable.getScanner(s);
		try {
			for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {

				String localObjectType = new String(rr.getValue("tags".getBytes(), "OBJECTTYPE".getBytes()));
				String localObjectId = new String(rr.getValue("tags".getBytes(), "OBJECTID".getBytes()));
				NavigableMap<byte[], byte[]> map = rr.getFamilyMap("tags".getBytes());
				Iterator<Entry<byte[], byte[]>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Entry<byte[], byte[]> entry = it.next();
					String key = new String(entry.getKey());
					if (!key.startsWith("OBJECT")) {
						int val = Bytes.toInt(entry.getValue());
						if (val > 0)
							ret.add(key);
					}

				}
			}
		} finally {
			scanner.close();
		}
		return ret.toArray(new String[] {});

	}

	public String[] getObjectIDs(String objectType, String... tags) throws IOException {
		List<String> ret = new ArrayList<String>();

		FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		SingleColumnValueFilter filter1 = new SingleColumnValueFilter("tags".getBytes(), "OBJECTTYPE".getBytes(),
				CompareOp.EQUAL, Bytes.toBytes(objectType));
		list.addFilter(filter1);

		for (String tag : tags) {
			SingleColumnValueFilter filter2 = new SingleColumnValueFilter("tags".getBytes(), tag.toUpperCase()
					.getBytes(), CompareOp.EQUAL, Bytes.toBytes(1));
			filter2.setFilterIfMissing(true);
			list.addFilter(filter2);
		}
		Scan s = new Scan();
		s.setFilter(list);
		s.setMaxVersions(1);
		ResultScanner scanner = htable.getScanner(s);
		try {
			for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {

				String localObjectType = new String(rr.getValue("tags".getBytes(), "OBJECTTYPE".getBytes()));
				String localObjectId = new String(rr.getValue("tags".getBytes(), "OBJECTID".getBytes()));
				ret.add(localObjectId);

			}
		} finally {
			scanner.close();
		}

		return ret.toArray(new String[] {});

	}
}
