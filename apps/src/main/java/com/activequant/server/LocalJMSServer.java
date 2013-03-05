package com.activequant.server;

import org.apache.activemq.broker.BrokerService;

/**
 * 
 * @author GhostRider
 *
 */
public class LocalJMSServer {
    private BrokerService broker;    
    public void start(String hostname, int port) throws Exception{
        broker = new BrokerService();
        broker.setUseJmx(false);        
        broker.addConnector("tcp://" + hostname + ":" + port);     
        broker.addConnector("stomp://"+hostname+":"+(port+2));        
        broker.start();
    }
    
    
    /**
     * Only needed in pure stanalone mode. 
     */
    public void runKeepaliveThread(){
    	Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true)
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}        	
        });
    	t.start();
    }
    public static void main(String[] args) throws NumberFormatException, Exception{
    	LocalJMSServer t = new LocalJMSServer();
    	if(args.length==0){
    		args = new String[]{"localhost", "61616"};
    	}
    	t.start(args[0], Integer.parseInt(args[1]));
    	t.runKeepaliveThread();        
    }
}
