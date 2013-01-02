package com.activequant.server;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.interfaces.transport.ITransportFactory;


public final class AQ2Server {

    // three BL objects.
    private LocalSoapServer ss;
    //
    private Logger log = Logger.getLogger(AQ2Server.class);

    private boolean runFlag = true;

    private void printBanner() throws InterruptedException{
    String banner = "   \n" +
" ______     ______     ______   __     __   __   ______\n" +    
"/\\  __ \\   /\\  ___\\   /\\__  _\\ /\\ \\   /\\ \\ / /  /\\  ___\\   \n" +
"\\ \\  __ \\  \\ \\ \\____  \\/_/\\ \\/ \\ \\ \\  \\ \\ \\'/   \\ \\  __\\   \n" +
" \\ \\_\\ \\_\\  \\ \\_____\\    \\ \\_\\  \\ \\_\\  \\ \\__|    \\ \\_____\\ \n" +
"  \\/_/\\/_/   \\/_____/     \\/_/   \\/_/   \\/_/      \\/_____/ \n" +
"                                                           \n" +
" ______     __  __     ______     __   __     ______  \n" +
"/\\  __ \\   /\\ \\/\\ \\   /\\  __ \\   /\\ -.\\ \\   /\\__  _\\ \n" +
"\\ \\ \\/\\_\\  \\ \\ \\_\\ \\  \\ \\  __ \\  \\ \\ \\-.  \\  \\/_/\\ \\/ \n" +
" \\ \\___\\_\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_\\\\\\_\\    \\ \\_\\ \n" +
"  \\/___/_/   \\/_____/   \\/_/\\/_/   \\/_/ \\/_/     \\/_/ \n" +
"                                                      \n" +
" __   __     ______     ______     ______   ______     ______\n" +    
"/\\ \\-./  \\   /\\  __ \\   /\\  ___\\   /\\__  _\\ /\\  ___\\   /\\  == \\   \n" +
"\\ \\ \\-./\\ \\  \\ \\  __ \\  \\ \\___  \\  \\/_/\\ \\/ \\ \\  __\\   \\ \\  __<   \n" +
" \\ \\_\\ \\ \\_\\  \\ \\_\\ \\_\\  \\/\\_____\\    \\ \\_\\  \\ \\_____\\  \\ \\_\\ \\_\\ \n" +
"  \\/_/  \\/_/   \\/_/\\/_/   \\/_____/     \\/_/   \\/_____/   \\/_/ /_/ \n" +
"                                                                  \n" +
"\n" +
" ______     ______     ______     __   __   ______     ______\n" +    
"/\\  ___\\   /\\  ___\\   /\\  == \\   /\\ \\ / /  /\\  ___\\   /\\  == \\   \n" +
"\\ \\___  \\  \\ \\  __\\   \\ \\  __<   \\ \\ \\'/   \\ \\  __\\   \\ \\  __<   \n" +
" \\/\\_____\\  \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\__|    \\ \\_____\\  \\ \\_\\ \\_\\ \n" +
"  \\/_____/   \\/_____/   \\/_/ /_/   \\/_/      \\/_____/   \\/_/ /_/ \n" +
"";                                                                 

    System.out.println(banner);
    System.out.println("Initializing system ... ");
    Thread.sleep(3000);
    }
    
    public AQ2Server() throws Exception {
        printBanner();
        log.info("Loading aq2server.properties from classpath.");
        log.info("Telling Java to prefer IPV4 ...");
        log.info("LocalHost() says: " + InetAddress.getLocalHost().getCanonicalHostName());
        System.setProperty("java.net.preferIPv4Stack" , "true");

        Properties properties = new Properties();
        properties.load(new FileInputStream("aq2server.properties"));
        log.info("Loaded.");

        // track back for statistical reasons.  
        if (isFalse(properties, "skipTrackback")) {
        	try{
        		URL u = new URL("http://www.zugtrader.com/thanks.html");
        		u.openStream();        		
        	}
        	catch(RuntimeException ex){}
        	catch(Exception ex){}
        }
        
        
       
        // changed start order, as the DAO layer could require a running HSQLDB. 
        if (isTrue(properties, "hbase.start")) {
            log.info("Starting mighty HBase ....");
            new LocalHBaseCluster(properties, properties.getProperty("zookeeper.host", null), Integer.parseInt(properties.getProperty("zookeeper.port", "2181"))).start();
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
        if (isTrue(properties, "startRandDatGen")) {
            log.info("Starting random market data generator....");
            ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"fwspring.xml"});
    		System.out.println("Starting up and fetching idf");
    		ITransportFactory transFac = appContext.getBean(ITransportFactory.class);		    		
            new RandomMarketDataGenerator(transFac);
            log.info("Random market data generator started.");
        } else {
            log.info("Not starting random market data generator, as it has been disabled.");
        }
        if (isTrue(properties, "hsqldb.start")) {
            log.info("Starting HSQLDB ....");
            new LocalHSQLDBServer().start(Integer.parseInt(properties.getProperty("hsqldb.port")));
            log.info("Starting HSQLDB succeeded.");
        } else {
            log.info("Not starting HSQDLB server, as it has been disabled.");
        }
        
        if (isTrue(properties, "soapserver.start")) {
            log.info("Starting soap server ....");
            ss = new LocalSoapServer(properties.getProperty("soapserver.hostname"), Integer.parseInt(properties.getProperty("soapserver.port")));
            ss.start();
            log.info("Starting soap server succeeded.");
        } else {
            log.info("Not starting soap server, as it has been disabled.");
        }

        // 
        if (isTrue(properties, "jetty.start")) {
            log.info("Starting JETTY ....");
            new LocalJettyServer(Integer.parseInt(properties.getProperty("jetty.port")), properties.getProperty("zookeeper.host", null), properties.getProperty("zookeeper.port", "2181")).start();
            log.info("Starting Jetty succeeded.");
        } else {
            log.info("Not starting JETTY server, as it has been disabled.");
        }
        
        // 
        while (runFlag) {
            Thread.sleep(250);
        }
    }

    private boolean isTrue(Properties properties, String key) {
        return properties.containsKey(key) && properties.getProperty(key).equals("true");
    }
    
    private boolean isFalse(Properties properties, String key) {
        return (!properties.containsKey(key)) ||  properties.getProperty(key).equals("false");
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
