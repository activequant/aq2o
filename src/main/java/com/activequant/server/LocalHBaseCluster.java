package com.activequant.server;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.master.HMaster;
import org.apache.hadoop.hbase.regionserver.HRegionServer;
import org.apache.hadoop.hbase.zookeeper.MiniZooKeeperCluster;

public class LocalHBaseCluster {

    private String hostName = "localhost"; 
    private int port = 2181; 
    private String dataDir= "./"; 
    public void start() throws Exception {
        Configuration config = HBaseConfiguration.create();
        // have to set a data directory. 
        config.set(HConstants.ZOOKEEPER_QUORUM, hostName);
        config.set(HConstants.HBASE_DIR, dataDir);    
        config.set(HConstants.CLUSTER_IS_LOCAL, "TRUE");
        
        
        //
        MiniZooKeeperCluster mzk = new MiniZooKeeperCluster(config);
        mzk.setDefaultClientPort(port);
        mzk.startup(new File("."+File.separator));
        
        HMaster master = new HMaster(config);
        master.start();
        
        HRegionServer hrs = new HRegionServer(config);
        HRegionServer.startRegionServer(hrs);
    }
    
    public static void main(String[] args) throws Exception {
    	new LocalHBaseCluster().start();
    }
    
}
