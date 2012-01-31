package com.activequant.server;

import org.apache.activemq.broker.BrokerService;

public class LocalJMSServer {
    private BrokerService broker;

    
    
    public void start(String hostname, int port) throws Exception{
        broker = new BrokerService();
        broker.addConnector("tcp://" + hostname + ":" + port);          
        broker.start();
    }
}
