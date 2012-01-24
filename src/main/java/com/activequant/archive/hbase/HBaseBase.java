package com.activequant.archive.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.log4j.Logger;

import com.activequant.utils.StringUtils;

class HBaseBase {

    private Logger log = Logger.getLogger(HBaseBase.class);
    protected HTable htable;

    HBaseBase(final String zookeeperQuorumHost, final String tableName) throws IOException {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zookeeperQuorumHost);
        HBaseAdmin admin = new HBaseAdmin(config);
        if (!admin.tableExists(tableName.getBytes())) {
            log.info("HTable doesn't exist. Creating it.");
            // create the table.
            HTableDescriptor desc = new HTableDescriptor(tableName.getBytes());
            HColumnDescriptor col1 = new HColumnDescriptor("numbers".getBytes());
            desc.addFamily(col1);
            admin.createTable(desc);
            boolean avail = admin.isTableAvailable(tableName.getBytes());
            log.info("HTable is available: " + avail);
        }
        htable = new HTable(config, tableName.getBytes());
        htable.setAutoFlush(false);
        htable.setScannerCaching(1000000);
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
