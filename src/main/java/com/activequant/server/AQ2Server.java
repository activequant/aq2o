package com.activequant.server;

import java.io.File;
import java.util.Properties;

import org.apache.activemq.broker.BrokerService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.master.HMaster;
import org.apache.hadoop.hbase.regionserver.HRegionServer;
import org.apache.hadoop.hbase.zookeeper.MiniZooKeeperCluster;
import org.apache.log4j.Logger;

public final class AQ2Server {

    // three BL objects. 
    
    private SoapServer ss;
    private BrokerService broker;
    // 
    private Logger log = Logger.getLogger(AQ2Server.class);

    private boolean runFlag = true;

    public AQ2Server() throws Exception {
        log.info("Loading aq2server.properties from classpath.");
        Properties properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("aq2server.properties"));
        log.info("Loaded.");

        if (isTrue(properties, "soapserver.start")) {
            log.info("Starting soap server ....");
            ss = new SoapServer(properties.getProperty("soapserver.hostname"), Integer.parseInt(properties.getProperty("soapserver.port")));
            ss.start();
            log.info("Starting soap server succeeded.");
        } else {
            log.info("Not starting soap server, as it has been disabled.");
        }

        if (isTrue(properties, "hbase.start")) {
            log.info("Starting mighty HBase ....");
            
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
            
            
            log.info("Starting HBase succeeded.");
        } else {
            log.info("Not starting HBase server, as it has been disabled.");
        }

        if (isTrue(properties, "activemq.start")) {
            log.info("Starting JMS ....");
            broker = new BrokerService();
            broker.addConnector("tcp://" + properties.getProperty("activemq.hostname") + ":" + properties.getProperty("activemq.port"));          
            broker.start();
            log.info("Starting JMS succeeded.");
        } else {
            log.info("Not starting JMS server, as it has been disabled.");
        }
        while (runFlag) {
            Thread.sleep(1000);
        }
    }

    private boolean isTrue(Properties properties, String key) {
        return properties.containsKey(key) && properties.getProperty(key).equals("true");
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new AQ2Server();
    }

}
