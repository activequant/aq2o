package com.activequant;

import java.io.IOException;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.util.FSUtils;

public class HBaseTest {
    /*
     * static HBaseConfiguration conf = null; static MiniDFSCluster dfs = null;
     * static MiniHBaseCluster hbase = null;
     * 
     * 
     * @SuppressWarnings("deprecation") public static void main(String[] args) {
     * try { int n = args.length > 0 && args[0] != null ?
     * Integer.parseInt(args[0]) : 4; conf = new HBaseConfiguration(); dfs = new
     * MiniDFSCluster(conf, 2, true, (String[]) null); // set file system to the
     * mini dfs just started up FileSystem fs = dfs.getFileSystem();
     * conf.set("fs.default.name", fs.getUri().toString()); Path parentdir =
     * fs.getHomeDirectory(); conf.set(HConstants.HBASE_DIR,
     * parentdir.toString()); fs.mkdirs(parentdir); FSUtils.setVersion(fs,
     * parentdir); conf.set(HConstants.REGIONSERVER_ADDRESS,
     * HConstants.DEFAULT_HOST + ":0"); // disable UI or it clashes for more
     * than one RegionServer conf.set("hbase.regionserver.info.port", "-1");
     * hbase = new MiniHBaseCluster(conf, n); // add close hook
     * Runtime.getRuntime().addShutdownHook(new Thread() { public void run() {
     * hbase.shutdown(); if (dfs != null) { try { FileSystem fs =
     * dfs.getFileSystem(); if (fs != null) fs.close(); } catch (IOException e)
     * { System.err.println("error closing file system: " + e); } try {
     * dfs.shutdown(); } catch (Exception e) { /*ignore
     *//*
        * } } } } ); } catch (Exception e) { e.printStackTrace(); } } // main
        */
} // MiniLocalHBase