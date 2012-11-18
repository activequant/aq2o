package com.activequant.server;

import java.io.File;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.master.HMaster;
import org.apache.hadoop.hbase.regionserver.HRegionServer;
import org.apache.hadoop.hbase.zookeeper.MiniZooKeeperCluster;

/**
 * plain and simple local hbase cluster code. Good for unit testing. Is also
 * used on the AQ Master Server
 * 
 * 
 * @author GhostRider
 * 
 */
public class LocalHBaseCluster {

	private final String zookeeperHost;
	private final int zookeeperPort;
	private String dataDir = "." + File.separator;
	private Properties properties = new Properties(); 
	
	public LocalHBaseCluster() {
		zookeeperHost = "127.0.0.1";
		zookeeperPort = 2181;
	}
	
	public LocalHBaseCluster(Properties properties, String zookeeperHost, int zookeeperPort) {
		this.zookeeperHost = zookeeperHost;
		this.zookeeperPort = zookeeperPort; 
		this.properties = properties; 
	}
	
	public void start() throws Exception {
		Configuration config = HBaseConfiguration.create();
		// have to set a data directory.
		config.set(HConstants.ZOOKEEPER_QUORUM, zookeeperHost);
		config.set(HConstants.HBASE_DIR, dataDir);
		// config.set(HConstants.CLUSTER_IS_LOCAL, "TRUE");

		//
		MiniZooKeeperCluster mzk = new MiniZooKeeperCluster(config);
		mzk.setDefaultClientPort(zookeeperPort);
		mzk.startup(new File(dataDir));
		
		// config.set("hbase.master.dns.interface", "eth0");
		if(properties.containsKey("fixBuggyNetworking")){
			config.set("aqHostOverride", properties.getProperty("overrideHostName"));	
		}
				
	    
				
		HMaster master = new HMaster(config);
		master.start();
		

		HRegionServer hrs = new HRegionServer(config);
		HRegionServer.startRegionServer(hrs);
	}

	public static void main(String[] args) throws Exception {
		new LocalHBaseCluster().start();
	}

}
