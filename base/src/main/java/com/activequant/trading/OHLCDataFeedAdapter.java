package com.activequant.trading;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.AQMessages.BaseMessage.CommandType;
import com.activequant.messages.Marshaller;

/**
 * Convenience wrapper to subscribe to an OHLC data feed. 
 * 
 * @author GhostRider
 *
 */
public class OHLCDataFeedAdapter {
	private Marshaller marshaller = new Marshaller();
	private Logger log = Logger.getLogger(OHLCDataFeedAdapter.class);
	private final String mdi;
	private final ITransportFactory transFac;
	private final TimeFrame resolution; 

	/**
	 * Internal event listener, this one will dispatch data on. 
	 */
	private IEventListener<byte[]> rawListener = new IEventListener<byte[]>() {
		@Override
		public void eventFired(byte[] event) {
			BaseMessage bm;
			try {
				bm = marshaller.demarshall(event);
				if (bm.getType().equals(CommandType.OHLC)) {
					OHLCV ohlc= marshaller
							.demarshall(((AQMessages.OHLC) bm
									.getExtension(AQMessages.OHLC.cmd)));
					processOHLCV(ohlc);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.warn("Exception: ", e);
			}
		}
	};

	// 
	private IEventListener<PersistentEntity> persListener = new IEventListener<PersistentEntity>(){
		@Override
		public void eventFired(PersistentEntity event) {
			if(event instanceof OHLCV)
				processOHLCV((OHLCV)event);
		}
		
	};
	
	/**
	 * override this in your class.
	 * 
	 * @param mds
	 */
	public void processOHLCV(OHLCV ohlc) {
		if (log.isDebugEnabled())
			log.debug("Received OHLC: " + ohlc);
	}

	/**
	 * public constructor.
	 * 
	 * @param mdi
	 * @param transFac
	 */
	public OHLCDataFeedAdapter(String mdi, TimeFrame resolution, ITransportFactory transFac) {
		this.mdi = mdi;
		this.transFac = transFac;
		this.resolution = resolution; 
	}

	/**
	 * Use it to start a feed.
	 * @throws TransportException 
	 */
	public void start() throws TransportException {

		// feels quite dirty, but ok. 
		OHLCV o  = new OHLCV();
		o.setResolutionInSeconds(resolution.getMinutes() * 60); 
		o.setMdiId(mdi);
		
		IReceiver r = transFac.getReceiver(o.getId());
		r.getRawEvent()
				.addEventListener(rawListener);
		// let's also add an old-format event listener.
		r.getMsgRecEvent().addEventListener(persListener);
	}

}
