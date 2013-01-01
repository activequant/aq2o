package com.activequant.component;

import java.util.Random;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;

/**
 * Simple and lightweight component bridge.
 * 
 * @author GhostRider
 * 
 */
public class ComponentMessagingLayer {

	private ITransportFactory transFac;
	private IServer server;
	private ComponentBase component;
	private final Logger log = Logger.getLogger(ComponentMessagingLayer.class);
	private final String randomId = "ID"+new Random().nextInt();
	 

	public ComponentMessagingLayer(ITransportFactory transFac)
			throws TransportException {
		// subscribe to the control channel.
		transFac.getReceiver(ETransportType.CONTROL.toString()).getRawEvent()
				.addEventListener(new IEventListener<byte[]>() {
					@Override
					public void eventFired(byte[] event) {
						process(event);
					}
				});
	}

	//
	private void process(byte[] bytes) {
		//
		try {
			String s = new String(bytes);
			String[] parts = s.split(";");
			String to = parts[0];
			String cmd = parts[1];			
			if(to.equals("server") && server!=null){
				// H = Heartbeat. 
				if(cmd.equals("H")){
					String id = parts[2];
					String componentName = parts[3];
					server.heartbeat(id, componentName);
				}
				// S = Status
				if(cmd.equals("S")){
					String id = parts[2];
					String statusMessage = parts[3];
					server.statusMessage(id, statusMessage);
				}
			}
			else if(to.equals(randomId)){
				if(component!=null){
					// target end point. 
				}				
			}
		} catch (Exception ex) {
			log.warn("Could not process byte message.", ex);
		}
		//
	}
		
	public void sendStatus(String message) throws TransportException, Exception {
		String msg = "server;S;"+randomId+";"+message+"\n";
		transFac.getPublisher(ETransportType.CONTROL.toString()).send(msg.getBytes());
	}
	
	public void sendHeartbeat() throws TransportException, Exception{
		String message = "server;H;"+randomId+";"+component.getName()+"\n";
		transFac.getPublisher(ETransportType.CONTROL.toString()).send(message.getBytes());
	}

	// ---
	public void setServer(IServer server) {
		this.server = server;
	}

	public void setComponent(ComponentBase component) {
		this.component = component;
	}

}
