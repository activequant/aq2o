package com.activequant.server;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Properties;

import org.apache.activemq.broker.BrokerService;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.CassandraDaemon;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class AQ2Server {

    // three BL objects. 
    private CassandraDaemon cassandraDaemon;
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

        // ApplicationContext appContext = new ClassPathXmlApplicationContext("serverspring.xml");

        if (isTrue(properties, "soapserver.start")) {
            log.info("Starting soap server ....");
            ss = new SoapServer(properties.getProperty("soapserver.hostname"), Integer.parseInt(properties.getProperty("soapserver.port")));
            ss.start();
            log.info("Starting soap server succeeded.");
        } else {
            log.info("Not starting soap server, as it has been disabled.");
        }

        if (isTrue(properties, "cassandra.start")) {
            log.info("Starting Cassandra ....");
            cassandraDaemon = new CassandraDaemon();
            cassandraDaemon.init(null);
            cassandraDaemon.start();
            log.info("Starting Cassandra succeeded.");
        } else {
            log.info("Not starting cassandra server, as it has been disabled.");
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

    @Test
    public void testInProcessCassandraServer() throws UnsupportedEncodingException, InvalidRequestException, UnavailableException, TimedOutException,
            TException, NotFoundException {
        Cassandra.Client client = getClient();

        ByteBuffer keyBuffer = ByteBuffer.wrap("key".getBytes("UTF-8"));
        ColumnParent cp = new ColumnParent("Numbers");
        Column column = new Column(ByteBuffer.wrap("BID".getBytes("UTF-8")));
        client.insert(keyBuffer, cp, column, ConsistencyLevel.ALL);

        ColumnPath cpath = new ColumnPath("Numbers");
        cpath.setColumn("BID".getBytes("utf-8"));

        ColumnOrSuperColumn got = client.get(keyBuffer, cpath, ConsistencyLevel.ALL);
        System.out.println(got);

    }

    /**
     * Gets a connection to the localhost client
     * 
     * @return
     * @throws TTransportException
     */
    private Cassandra.Client getClient() throws TTransportException {
        TTransport tr = new TSocket("localhost", 9160);
        TProtocol proto = new TBinaryProtocol(tr);
        Cassandra.Client client = new Cassandra.Client(proto);
        tr.open();
        return client;
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new AQ2Server();
    }

}
