package com.activequant.trading;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
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
				} else
				// really need to make a proper converter finally.
				if (bm.getType().equals(CommandType.SERVER_TIME)) {

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
	public void processMarketDataSnapshot(MarketDataSnapshot mds) {
		if (log.isDebugEnabled())
			log.debug("Received MDS: " + mds);
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
		transFac.getReceiver(ETransportType.MARKET_DATA, mdi).getRawEvent()
				.addEventListener(rawListener);
	}

}
