package com.activequant.archive.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.jruby.RubyProcess.Sys;

import com.activequant.domainmodel.TimeFrame;

public class HBaseUpgrader {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Configuration config = HBaseConfiguration.create();
		config.set("hbase.zookeeper.quorum", args[0]);
		HBaseAdmin admin = new HBaseAdmin(config);
		for (TimeFrame tf : TimeFrame.values()) {

			String tableName = "TSDATA_" + tf.toString();
			System.out.println("processing " + tableName);
			// admin.disableTable(tableName.getBytes());
			if (admin.tableExists(tableName)) {
				HTableDescriptor h = admin.getTableDescriptor(tableName
						.getBytes());
				//
				if (!h.hasFamily("fields".getBytes())) {
					System.out.println("Table doesn't have fields family.");
					admin.disableTable(tableName);
					//
					HColumnDescriptor col2 = new HColumnDescriptor(
							"fields".getBytes());
					h.addFamily(col2);
					admin.modifyTable(tableName.getBytes(), h);
					//
					admin.enableTableAsync(tableName);
					System.out.println("Table enabled.");
				}
				//
				//
				//
			} else {
				System.out.println("Table doesn't exist, not touching it.");
			}
			//
			// HTableDescriptor desc = new
			// HTableDescriptor(tableName.getBytes());
			// HColumnDescriptor col2 = new
			// HColumnDescriptor("fields3".getBytes());
			// desc.addFamily(col2);
			// admin.modifyTable(tableName.getBytes(), desc);
			// admin.enableTable(tableName.getBytes());
		}
	}

}
