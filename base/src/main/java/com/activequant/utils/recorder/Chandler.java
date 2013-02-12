package com.activequant.utils.recorder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.AQMessages.BaseMessage.CommandType;
import com.activequant.messages.Marshaller;
import com.activequant.messages.MessageFactory;

/**
 * A chandler makes candles.
 * 
 * @author GhostRider
 * 
 */
public class Chandler extends ComponentBase {

	private ITransportFactory transFac;
	private Logger log = Logger.getLogger(Chandler.class);
	final Timer t = new Timer(true);
	final int timeFrameInMs;
	final TimeFrame tf;
	private final Marshaller marshaller = new Marshaller();
	private final MessageFactory mf = new MessageFactory();

	class InternalTimerTask extends TimerTask {
		@Override
		public void run() {
			List<OHLCV> candleList = new ArrayList<OHLCV>();
			synchronized (candles) {
				Iterator<Entry<String, OHLCV>> it = candles.entrySet()
						.iterator();
				while (it.hasNext()) {
					OHLCV o = it.next().getValue();
					candleList.add(o.clone());
					o.clear();
				}
			}
			process(candleList);
			log.info("Drew " + candleList.size() + ". ");
			t.schedule(
					new InternalTimerTask(),
					(timeFrameInMs - System.currentTimeMillis() % timeFrameInMs));
		}

		public void process(List<OHLCV> candleList) {
			for (OHLCV o : candleList) {
				if (o.getOpen() != null && o.getOpen()!=Double.NaN) {
					System.out.println(o.getMdiId() + " - " + o.getOpen()
							+ " - " + o.getClose());
					try {						
						BaseMessage bm = mf.ohlc(o.getTimeStamp(), o.getMdiId(), o.getOpen(),
								o.getHigh(), o.getLow(), o.getClose());

						// let's convert that candle to a byte array.
						transFac.getPublisher(o.getId()).send(bm.toByteArray());
					} catch (TransportException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public Chandler(ITransportFactory transFac, String mdiFile, TimeFrame tf)
			throws Exception {
		super("Chandler " + tf, transFac);
		this.timeFrameInMs = tf.getMinutes() * 60 * 1000;
		System.out.println("Starting up and fetching idf");
		this.transFac = transFac;
		this.tf = tf;
		subscribe(mdiFile);
		t.schedule(new InternalTimerTask(),
				(timeFrameInMs - System.currentTimeMillis() % timeFrameInMs));

	}

	private void subscribe(String mdiFile) throws IOException,
			TransportException {

		List<String> instruments = new ArrayList<String>();

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(mdiFile)));
		String l = br.readLine();
		while (l != null) {
			if (!l.startsWith("#") && !l.isEmpty()) {
				String symbol = l;
				int depth = 1;
				if (l.indexOf(";") != -1) {
					String[] s = l.split(";");
					symbol = s[0];
					depth = Integer.parseInt(s[1]);
				}
				instruments.add(symbol);
			}
			l = br.readLine();
		}
		for (String s : instruments) {
			log.info("Subscribing to " + s);
			transFac.getReceiver(ETransportType.MARKET_DATA, s).getRawEvent()
					.addEventListener(new IEventListener<byte[]>() {
						@Override
						public void eventFired(byte[] event) {
							BaseMessage bm;
							try {
								bm = marshaller.demarshall(event);
								if (log.isDebugEnabled())
									log.debug("Event type: " + bm.getType());
								if (bm.getType().equals(CommandType.MDS)) {
									MarketDataSnapshot mds = marshaller
											.demarshall(((AQMessages.MarketDataSnapshot) bm
													.getExtension(AQMessages.MarketDataSnapshot.cmd)));
									process(mds);
								}
							} catch (Exception e) {
								e.printStackTrace();
								log.warn("Exception: ", e);
							}
						}
					});

		}
	}

	private Map<String, OHLCV> candles = new HashMap<String, OHLCV>();

	private OHLCV getCandle(String mdiId) {
		OHLCV ret = candles.get(mdiId);
		if (ret == null) {
			ret = new OHLCV();
			ret.setResolutionInSeconds(timeFrameInMs / 1000);
			ret.setMdiId(mdiId);
			synchronized (candles) {
				candles.put(mdiId, ret);
			}
		}
		return ret;
	}

	protected void process(MarketDataSnapshot mds) {
		if (mds == null)
			return;

		String seriesId = mds.getMdiId();
		Double bid = null;
		Double ask = null;
		//
		if (mds.getBidSizes() != null && mds.getBidSizes().length > 0) {
			double bestBidPx = mds.getBidPrices()[0];
			double bestBidQ = mds.getBidSizes()[0];
			bid = bestBidPx;
			System.out.print("B");
		}
		if (mds.getAskSizes() != null && mds.getAskSizes().length > 0) {
			double bestAskPx = mds.getAskPrices()[0];
			double bestAskQ = mds.getAskSizes()[0];
			ask = bestAskPx;
			System.out.print("A");
		}
		//
		if (bid != null && ask != null) {
			double mid = (bid + ask) / 2.0;
			OHLCV o = getCandle(seriesId);
			o.update(mds.getTimeStamp(), mid);
			System.out.print("O");
		}
	}

	@Override
	public String getDescription() {
		return "Chandler " + tf;
	}

}
