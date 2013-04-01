package com.activequant.trading;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.AQMessages.BaseMessage.CommandType;
import com.activequant.messages.AQMessages.ValueSet;
import com.activequant.messages.Marshaller;

/**
 * Convenience wrapper to subscribe to value set feeds
 * 
 * @author GhostRider
 * 
 */
public class ValueSetAdapter {
	private Marshaller marshaller = new Marshaller();
	private Logger log = Logger.getLogger(ValueSetAdapter.class);
	private final String channel;
	private final ITransportFactory transFac;

	/**
	 * Internal event listener, this one will dispatch data on.
	 */
	private IEventListener<byte[]> rawListener = new IEventListener<byte[]>() {
		@Override
		public void eventFired(byte[] event) {
			BaseMessage bm;
			try {
				bm = marshaller.demarshall(event);
				if (bm.getType().equals(CommandType.VALUESET)) {
					ValueSet vs = bm.getExtension(ValueSet.cmd);
					handle(vs);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("Exception: ", e);
			}
		}
	};

	/**
	 * override this in your class.
	 * 
	 * @param mds
	 */
	public void handle(ValueSet vs) {
		if (log.isDebugEnabled())
			log.debug("Received value set: " + vs);
	}

	/**
	 * public constructor.
	 * 
	 * @param mdi
	 * @param transFac
	 */
	public ValueSetAdapter(String channel, ITransportFactory transFac) {
		this.channel = channel;
		this.transFac = transFac;
	}

	/**
	 * Use it to start a feed.
	 * 
	 * @throws TransportException
	 */
	public void start() throws TransportException {
		IReceiver r = transFac.getReceiver(channel);
		r.getRawEvent().addEventListener(rawListener);
	}

}
