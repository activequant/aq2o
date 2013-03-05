package com.activequant.server;

import java.io.File;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.master.HMaster;
import org.apache.hadoop.hbase.regionserver.HRegionServer;
import org.apache.hadoop.hbase.zookeeper.MiniZooKeeperCluster;
import org.apache.log4j.Logger;

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
	private Logger log = Logger.getLogger(LocalHBaseCluster.class);
	
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
		config.set("hbase.master.info.port", "-10");
		config.set("hbase.regionserver.info.port", "-10");
		// config.set(HConstants.CLUSTER_IS_LOCAL, "TRUE");

		log.info("Starting zookeeper.");
		//
		MiniZooKeeperCluster mzk = new MiniZooKeeperCluster(config);
		mzk.setDefaultClientPort(zookeeperPort);
		mzk.startup(new File(dataDir));
		
		// config.set("hbase.master.dns.interface", "eth0");
		if(properties.containsKey("fixBuggyNetworking") && properties.get("fixBuggyNetworking").equals("true")){
			config.set("aqHostOverride", properties.getProperty("overrideHostName"));	
		}
				
		log.info("Starting hmaster.");				
		HMaster master = new HMaster(config);
		master.start();
		
		log.info("Starting regionserver.");
		HRegionServer hrs = new HRegionServer(config);
		HRegionServer.startRegionServer(hrs);
		// 
		
		// 
	}

	public static void main(String[] args) throws Exception {
		new LocalHBaseCluster().start();
	}

}
