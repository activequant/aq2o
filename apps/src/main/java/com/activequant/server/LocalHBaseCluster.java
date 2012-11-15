package com.activequant.server;

import java.io.File;

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

	private String hostName = "127.0.0.1";
	private int port = 2181;
	private String dataDir = "." + File.separator;

	public void start() throws Exception {
		Configuration config = HBaseConfiguration.create();
		// have to set a data directory.
		config.set(HConstants.ZOOKEEPER_QUORUM, hostName);
		config.set(HConstants.HBASE_DIR, dataDir);
		// config.set(HConstants.CLUSTER_IS_LOCAL, "TRUE");

		//
		MiniZooKeeperCluster mzk = new MiniZooKeeperCluster(config);
		mzk.setDefaultClientPort(port);
		mzk.startup(new File(dataDir));

		HMaster master = new HMaster(config);
		master.start();

		HRegionServer hrs = new HRegionServer(config);
		HRegionServer.startRegionServer(hrs);
	}

	public static void main(String[] args) throws Exception {
		new LocalHBaseCluster().start();
	}

}
