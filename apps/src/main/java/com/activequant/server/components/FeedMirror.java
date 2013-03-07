package com.activequant.server.components;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;

/**
 * sample component that creates random market data.
 * 
 * @author GhostRider
 * 
 */
public class FeedMirror extends ComponentBase {

	//
	IPublisher[] publishers;
	IReceiver[] receivers;
	private String[] channels = new String[] {};
	ITransportFactory transFac;

	//
	public FeedMirror(ITransportFactory remoteNode, ITransportFactory localNode)
			throws Exception {
		super("FeedMirror", localNode);
		//
		String channelString = super.properties.getProperty("channels", "");
		if (channelString.length() > 0) {
			channels = channelString.split(",");
		}
		publishers = new IPublisher[channels.length];
		receivers = new IReceiver[channels.length];
		//
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					subscribe();
				} catch (TransportException e) {
					e.printStackTrace();
				}
			}
		});

		// let's start a separate thread for this component.
		t.start();
	}

	private void subscribe() throws TransportException {
		//
		for (String s : channels) {
			//
			IReceiver receiver = transFac.getReceiver(s);
			final IPublisher publisher = transFac.getPublisher(s);
			receiver.getRawEvent().addEventListener(
					new IEventListener<byte[]>() {
						@Override
						public void eventFired(byte[] arg0) {
							try {
								publisher.send(arg0);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}
		//
	}

	//
	@Override
	public String getDescription() {
		//
		return "The feed mirror is a one way data feed mirror. It relays data from one point to another, but not vice versa.";

	}

}
