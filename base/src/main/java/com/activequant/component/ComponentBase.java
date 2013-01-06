package com.activequant.component;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.ITransportFactory;

/**
 * A component can run as a standalone or as an embedded application. It has to
 * connect to the master server.
 * 
 * @author GhostRider
 * 
 */
public abstract class ComponentBase {

	protected final ITransportFactory transFac;
	protected final ComponentMessagingLayer compML; 
	protected final Logger log = Logger.getLogger(ComponentBase.class);
	private final String name; 

	public ComponentBase(String name, ITransportFactory transFac) throws Exception {
		this.transFac = transFac;
		this.name = name; 
		this.compML = new ComponentMessagingLayer(transFac);
		this.compML.setComponent(this);
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					heartbeat();
				} catch (TransportException e) {
					log.warn("Could not send heartbeat", e);
				} catch (Exception e) {
					log.warn("Could not send heartbeat", e);
				}
			}
		}, 10 * 1000, 10 * 1000);
		heartbeat();
		sendStatus("Constructed.");
	}

	public String getName(){return name;}
	
	protected void heartbeat() throws TransportException, Exception {
		compML.sendHeartbeat();
	}
	
	protected void sendStatus(String message) throws TransportException, Exception{
		compML.sendStatus(message);
	}
	
	
	/**
	 * Get requests should result in a set response.
	 * You should override this function in order to handle specific get requests. 
	 *  
	 * NOT DONE YET - WORK IN PROGRESS. THINKING ABOUT WHETHER I SHOULD USE SOME RPC APPROACH OR NOT. 
	 * (lock-in to java comes into mind)
	 * 
	 * @param function
	 */
	protected void request(String function){
		
	}

	/**
	 * method that gets called when a so-called custom message hsa been sent to this component.  
	 * 
	 * @param customMessage
	 */
	public void customMessage(String customMessage){}
	
	public abstract String getDescription();
	
	

}
