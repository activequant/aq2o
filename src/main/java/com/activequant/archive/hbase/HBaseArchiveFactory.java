package com.activequant.archive.hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.IArchiveReader;
import com.activequant.archive.IArchiveWriter;
import com.activequant.domainmodel.TimeFrame;

public class HBaseArchiveFactory implements IArchiveFactory {

    private final Map<TimeFrame, IArchiveReader> readers = new HashMap<TimeFrame, IArchiveReader>();
    private final Map<TimeFrame, IArchiveWriter> writers = new HashMap<TimeFrame, IArchiveWriter>();
    private final String zookeeperHost;

    public HBaseArchiveFactory(final String zookeeperHost) {
        this.zookeeperHost = zookeeperHost;
    }

    public HBaseArchiveFactory() {
        // should be taken from a properties file. 
        this.zookeeperHost = "localhost";
    }

    public synchronized IArchiveReader getReader(TimeFrame tf) {
        if (readers.get(tf) == null)
            try {
                IArchiveReader reader = new HBaseArchiveReader(zookeeperHost, tf);
                readers.put(tf, reader);

            } catch (IOException e) {
                e.printStackTrace();
            }
        return readers.get(tf);
    }

    public synchronized IArchiveWriter getWriter(TimeFrame tf) {
        if (writers.get(tf) == null)
            try {
                IArchiveWriter writer = new HBaseArchiveWriter(zookeeperHost, tf);
                writers.put(tf, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return writers.get(tf);
    }

}
