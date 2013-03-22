package com.activequant.trading;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
import com.activequant.domainmodel.streaming.Tick;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.AQMessages.BaseMessage.CommandType;
import com.activequant.messages.Marshaller;

/**
 * Convenience wrapper. 
 * 
 * @author ustaudinger
 *
 */
public class MarketDataFeedAdapter {
	private Marshaller marshaller = new Marshaller();
	private Logger log = Logger.getLogger(MarketDataFeedAdapter.class);
	private final String mdi;
	private final ITransportFactory transFac;

	/**
	 * 
	 */
	private IEventListener<byte[]> rawListener = new IEventListener<byte[]>() {
		@Override
		public void eventFired(byte[] event) {
			BaseMessage bm;
			try {
				bm = marshaller.demarshall(event);
				if (bm.getType().equals(CommandType.MDS)) {
					MarketDataSnapshot mds = marshaller
							.demarshall(((AQMessages.MarketDataSnapshot) bm
									.getExtension(AQMessages.MarketDataSnapshot.cmd)));
					processMarketDataSnapshot(mds);
				}
				else if(bm.getType().equals(CommandType.TICK)){
					Tick t = marshaller.demarshall(((AQMessages.Tick) bm
							.getExtension(AQMessages.Tick.cmd)));
					processTick(t);
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
			if(event instanceof MarketDataSnapshot)
				processMarketDataSnapshot((MarketDataSnapshot)event);
		}
		
	};
	
	/**
	 * override this in your class.
	 * 
	 * @param mds
	 */
	public void processMarketDataSnapshot(MarketDataSnapshot mds) {
		if (log.isDebugEnabled())
			log.debug("Received MDS: " + mds);
	}
	
	/**
	 * override this in your class.
	 * 
	 * @param tick
	 */
	public void processTick(Tick tick){
		if (log.isDebugEnabled())
			log.debug("Received Tick: " + tick);
	}

	/**
	 * public constructor.
	 * 
	 * @param mdi
	 * @param transFac
	 */
	public MarketDataFeedAdapter(String mdi, ITransportFactory transFac) {
		this.mdi = mdi;
		this.transFac = transFac;
	}

	/**
	 * Use it to start a feed.
	 * @throws TransportException 
	 */
	public void start() throws TransportException {
		IReceiver r = transFac.getReceiver(ETransportType.MARKET_DATA, mdi);
		r.getRawEvent()
				.addEventListener(rawListener);
		// let's also add an old-format event listener.
		r.getMsgRecEvent().addEventListener(persListener);
	}

}
