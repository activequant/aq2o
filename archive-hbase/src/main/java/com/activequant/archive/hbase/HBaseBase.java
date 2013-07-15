package com.activequant.archive.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.log4j.Logger;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.utils.StringUtils;

/**
 * 
 * 
 * 
 * @author GhostRider
 *
 * Changelog: 
 * 14.3.2013 setting scanner cache to 100000
 *
 */
class HBaseBase {

	protected Configuration config = null; 
    private Logger log = Logger.getLogger(HBaseBase.class);
    protected HTable htable;

    HBaseBase(final String zookeeperQuorumHost, final String tableName) throws IOException {
        this(zookeeperQuorumHost, 2181, tableName);
    }

    HBaseBase(final String zookeeperQuorumHost, final int zookeeperPort, final String tableName) throws IOException {
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zookeeperQuorumHost+":"+zookeeperPort);               
        HBaseAdmin admin = new HBaseAdmin(config);
        if (!admin.tableExists(tableName.getBytes())) {
            log.info("HTable doesn't exist. Creating it.");
            // create the table.
            HTableDescriptor desc = new HTableDescriptor(tableName.getBytes());
            HColumnDescriptor col1 = new HColumnDescriptor("numbers".getBytes());
            desc.addFamily(col1);
            HColumnDescriptor col2 = new HColumnDescriptor("fields".getBytes());
            desc.addFamily(col2);
            admin.createTable(desc);
            boolean avail = admin.isTableAvailable(tableName.getBytes());
            log.info("HTable is available: " + avail);
        }
        admin.close();        
        htable = new HTable(config, tableName.getBytes());
        htable.setAutoFlush(false);        
        htable.setScannerCaching(1000000);
    }

    protected ResultScanner getScanner(final String instrumentId, final TimeStamp startTimeStamp, final TimeStamp stopTimeStamp) throws IOException {
        String startKey = instrumentId + "_" + padded(startTimeStamp.toString());
        String stopKey = instrumentId + "_" + padded(stopTimeStamp.toString());

        Scan s = new Scan(startKey.getBytes(), stopKey.getBytes());
        s.setCacheBlocks(true);
        s.setMaxVersions(1);      
        s.setCaching(10000);
        ResultScanner scanner = htable.getScanner(s);
        return scanner;
    }
    
    public String padded(String key){
        int refLength = "1235526680000000000".length(); 
        if(key.length()<refLength)
        {
            String padding = StringUtils.repeat('0', refLength - key.length());
            key = padding+key;
        }
        return key; 
    }
    
    @Deprecated
    HBaseBase(final String tableName) throws IOException {
        this("localhost", tableName);
    }

}
