package com.activequant.archive.hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.IArchiveReader;
import com.activequant.archive.IArchiveWriter;
import com.activequant.domainmodel.TimeFrame;

/**
 * Instantiates readers and writers for different time frames. 
 * Stores them internally in a hashmap. 
 * Access to the getReader and getWriter methods is synchronized. 
 * 
 * @author ustaudinger
 *
 */
public class HBaseArchiveFactory implements IArchiveFactory {

    private final Map<TimeFrame, IArchiveReader> readers = new HashMap<TimeFrame, IArchiveReader>();
    private final Map<TimeFrame, IArchiveWriter> writers = new HashMap<TimeFrame, IArchiveWriter>();
    private final String zookeeperHost;
    private final int zookeeperPort; 

    public HBaseArchiveFactory(final String zookeeperHost) {
        this.zookeeperHost = zookeeperHost;
        this.zookeeperPort = 2181; 
    }

    public HBaseArchiveFactory(final String zookeeperHost, final int zookeeperPort) {
        this.zookeeperHost = zookeeperHost;
        this.zookeeperPort = zookeeperPort; 
    }
    
    public synchronized IArchiveReader getReader(TimeFrame tf) {
        if (readers.get(tf) == null)
            try {
                IArchiveReader reader = new HBaseArchiveReader(zookeeperHost, zookeeperPort, tf);
                readers.put(tf, reader);

            } catch (IOException e) {
                e.printStackTrace();
            }
        return readers.get(tf);
    }

    public synchronized IArchiveWriter getWriter(TimeFrame tf) {
        if (writers.get(tf) == null)
            try {
                IArchiveWriter writer = new HBaseArchiveWriter(zookeeperHost, zookeeperPort, tf);
                writers.put(tf, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return writers.get(tf);
    }

}
