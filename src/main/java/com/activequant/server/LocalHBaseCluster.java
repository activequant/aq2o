package com.activequant.server;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.master.HMaster;
import org.apache.hadoop.hbase.regionserver.HRegionServer;
import org.apache.hadoop.hbase.zookeeper.MiniZooKeeperCluster;

public class LocalHBaseCluster {

    public void start() throws Exception {
        Configuration config = HBaseConfiguration.create();
        // have to set a data directory. 
        
        //
        MiniZooKeeperCluster mzk = new MiniZooKeeperCluster(config);
        mzk.setClientPort(2181);
        mzk.startup(new File("."+File.separator));
        
        HMaster master = new HMaster(config);
        master.start();
        
        HRegionServer hrs = new HRegionServer(config);
        HRegionServer.startRegionServer(hrs);
        
        
    }
    
}
