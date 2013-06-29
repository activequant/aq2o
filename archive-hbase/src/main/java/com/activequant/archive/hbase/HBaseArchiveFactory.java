package com.activequant.archive.hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.activequant.domainmodel.TimeFrame;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveReader;
import com.activequant.interfaces.archive.IArchiveWriter;

/**
 * Instantiates readers and writers for different time frames. Stores them
 * internally in a hashmap. Access to the getReader and getWriter methods is
 * synchronized.
 * 
 * @author ustaudinger
 * 
 */
public class HBaseArchiveFactory implements IArchiveFactory {

	private final Map<TimeFrame, IArchiveReader> readers = new HashMap<TimeFrame, IArchiveReader>();
	private final Map<TimeFrame, IArchiveWriter> writers = new HashMap<TimeFrame, IArchiveWriter>();
	private final String zookeeperHost;
	private final int zookeeperPort;
	private boolean reuseFlag;

	public HBaseArchiveFactory(final String zookeeperHost) {
		this.zookeeperHost = zookeeperHost;
		this.zookeeperPort = 2181;
	}

	public HBaseArchiveFactory(final String zookeeperHost,
			final int zookeeperPort) {
		this.zookeeperHost = zookeeperHost;
		this.zookeeperPort = zookeeperPort;
	}

	public void setReuseReaders(boolean reuseFlag) {
		this.reuseFlag = reuseFlag;
	}

	public synchronized IArchiveReader getReader(TimeFrame tf) {
		if (readers.get(tf) == null)
			try {
				IArchiveReader reader = new HBaseArchiveReader(zookeeperHost,
						zookeeperPort, tf);
				if (reuseFlag)
					readers.put(tf, reader);
				else
					return reader;

			} catch (IOException e) {
				e.printStackTrace();
			}
		return readers.get(tf);
	}

	public synchronized IArchiveWriter getWriter(TimeFrame tf) {
		if (writers.get(tf) == null)
			try {
				IArchiveWriter writer = new HBaseArchiveWriter(zookeeperHost,
						zookeeperPort, tf);
				if (reuseFlag)
					writers.put(tf, writer);
				else
					return writer;
			} catch (IOException e) {
				e.printStackTrace();
			}
		return writers.get(tf);
	}

}
