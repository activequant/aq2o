package com.activequant.server.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.activequant.component.ComponentMessagingLayer;
import com.activequant.component.IServer;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.ITransportFactory;

public class ServerComponent implements IServer {
	
	// 
	private final Map<String, Long> componentLastSeen = new HashMap<String, Long>();
	private final Map<String, String> componentIdToName = new HashMap<String, String>();
	private final Map<String, String> componentDescriptions = new HashMap<String, String>();
	private final Logger log = Logger.getLogger(ServerComponent.class);
	
	// 
	private ComponentMessagingLayer cml;

	@Autowired
	private ITransportFactory transFac; 
	
	public ServerComponent() {}
	public void init() throws TransportException{
		cml = new ComponentMessagingLayer(transFac);
		cml.setServer(this);
	}
	
	@Override
	public void heartbeat(String componentId, String component) {
		if(!componentLastSeen.containsKey(componentId))
		{
			// request a description. 
			try {
				cml.requestDescription(componentId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		componentLastSeen.put(componentId, System.currentTimeMillis());
		componentIdToName.put(componentId, component);
	}

	@Override
	public void statusMessage(String componentId, String statusMessage) {
	}

	public Map<String, Long> getComponentLastSeen() {
		return componentLastSeen;
	}

	public Map<String, String> getComponentIdToName() {
		return componentIdToName;
	}

	public Map<String, String> getComponentDescriptions() {
		return componentDescriptions;
	}
	
	@Override
	public void componentDescription(String arg0, String arg1) {
		log.info("Component "+ arg0 +" sent description.");
		componentDescriptions.put(arg0, arg1);
	}
	
}
