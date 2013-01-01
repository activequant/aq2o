package com.activequant.component;

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
		}, 2 * 60 * 1000, 2 * 60 * 1000);
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
	
}
