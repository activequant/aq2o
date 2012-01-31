package com.activequant.server;

import java.util.Properties;

import org.apache.log4j.Logger;

public final class AQ2Server {

    // three BL objects.

    private LocalSoapServer ss;
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
            ss = new LocalSoapServer(properties.getProperty("soapserver.hostname"), Integer.parseInt(properties.getProperty("soapserver.port")));
            ss.start();
            log.info("Starting soap server succeeded.");
        } else {
            log.info("Not starting soap server, as it has been disabled.");
        }

        if (isTrue(properties, "hbase.start")) {
            log.info("Starting mighty HBase ....");
            new LocalHBaseCluster().start();
            log.info("Starting HBase succeeded.");
        } else {
            log.info("Not starting HBase server, as it has been disabled.");
        }

        if (isTrue(properties, "activemq.start")) {
            log.info("Starting JMS ....");
            new LocalJMSServer().start(properties.getProperty("activemq.hostname"), Integer.parseInt(properties.getProperty("activemq.port")));
            log.info("Starting JMS succeeded.");
        } else {
            log.info("Not starting JMS server, as it has been disabled.");
        }
        while (runFlag) {
            Thread.sleep(250);
        }
    }

    private boolean isTrue(Properties properties, String key) {
        return properties.containsKey(key) && properties.getProperty(key).equals("true");
    }
    
    public void stop(){
        runFlag = false; 
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new AQ2Server();
    }

    public LocalSoapServer getSoapServer() {
        return ss;
    }

}
