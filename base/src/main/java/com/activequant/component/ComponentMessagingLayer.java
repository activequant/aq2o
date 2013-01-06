package com.activequant.component;

import java.util.Map;
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
	private final String randomId = "ID" + new Random().nextInt();

	public ComponentMessagingLayer(ITransportFactory transFac)
			throws TransportException {
		this.transFac = transFac;
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
			if (log.isDebugEnabled())
				log.debug("RECV:" + s);
			String[] parts = s.split(";");
			String to = parts[0];
			String cmd = parts[1];
			if (to.equals("server") && server != null) {
				// H = Heartbeat.
				if (cmd.equals("H")) {
					String id = parts[2];
					String componentName = parts[3];
					server.heartbeat(id, componentName);
				}
				// S = Status
				else if (cmd.equals("S")) {
					String id = parts[2];
					String statusMessage = parts[3];
					server.statusMessage(id, statusMessage);
				}
				// SD = Description
				else if (cmd.equals("SD")) {
					server.componentDescription(parts[2], parts[3]);
				}
			} else {
				if (server == null) {
					if (to.equals(randomId)) {
						if (component != null) {
							// target end point. let's check the command.
							if (cmd.equals("RD"))
								sendDescription(component.getDescription());
							if(cmd.equals("C"))
								component.customMessage(parts[2]);
						}
					}
				}
			}
		} catch (Exception ex) {
			log.warn("Could not process byte message.", ex);
		}
		//
	}

	//
	public void sendStatus(String message) throws TransportException, Exception {
		String msg = "server;S;" + randomId + ";" + message;
		transFac.getPublisher(ETransportType.CONTROL.toString()).send(
				msg.getBytes());
	}

	//
	public void sendHeartbeat() throws TransportException, Exception {
		String message = "server;H;" + randomId + ";" + component.getName();
		transFac.getPublisher(ETransportType.CONTROL.toString()).send(
				message.getBytes());
	}

	//
	public void sendDescription(String description) throws TransportException,
			Exception {
		String message = "server;SD;" + randomId + ";" + description;
		transFac.getPublisher(ETransportType.CONTROL.toString()).send(
				message.getBytes());
	}

	//
	public void requestDescription(String componentId)
			throws TransportException, Exception {
		String message = componentId + ";RD";
		transFac.getPublisher(ETransportType.CONTROL.toString()).send(
				message.getBytes());
	}

	public void customMessage(String componentId, String msg) throws TransportException, Exception{
		String message = componentId + ";C;"+msg;
		transFac.getPublisher(ETransportType.CONTROL.toString()).send(
				message.getBytes());
	}
	
	//
	public void response(String component, String function,
			Map<String, Object> map) throws TransportException, Exception {
		//
		String message = component + ";S;" + function + ";";
		transFac.getPublisher(ETransportType.CONTROL.toString()).send(
				message.getBytes());
	}

	//
	public void request(String componentId, String function,
			Map<String, Object> map) throws TransportException, Exception {
		//
		String message = componentId + ";G;" + function + ";";
		transFac.getPublisher(ETransportType.CONTROL.toString()).send(
				message.getBytes());
	}

	// ---
	public void setServer(IServer server) {
		this.server = server;
	}

	public void setComponent(ComponentBase component) {
		this.component = component;
	}

}
