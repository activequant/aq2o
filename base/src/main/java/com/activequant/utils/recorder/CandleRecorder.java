package com.activequant.utils.recorder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveWriter;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.AQMessages.BaseMessage.CommandType;
import com.activequant.messages.AQMessages;
import com.activequant.messages.Marshaller;

/**
 * A market data snapshot recorder.
 * 
 * @author GhostRider
 * 
 */
public class CandleRecorder extends ComponentBase {

	private IArchiveFactory archiveFactory;
	private ITransportFactory transFac;
	private Logger log = Logger.getLogger(CandleRecorder.class);
	final Timer t = new Timer(true);
	final long collectionPhase = 5000l;
	private TimeFrame tf;
	private final ConcurrentLinkedQueue<OHLCV> collectionList = new ConcurrentLinkedQueue<OHLCV>();

	private final Marshaller marshaller = new Marshaller();
	private final IArchiveWriter writer;

	class InternalTimerTask extends TimerTask {
		@Override
		public void run() {
			Object o = collectionList.poll();
			int counter = 0;
			while (o != null) {
				counter++;
				store((OHLCV) o);
				o = collectionList.poll();
			}
			log.info("Collected " + counter + " events. ");
			if (counter > 0) {
				try {
					writer.commit();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			t.schedule(new InternalTimerTask(),
					(collectionPhase - System.currentTimeMillis()
							% collectionPhase));
		}

		public void store(OHLCV o) {
			if (o == null)
				return;
			if(o.getOpen().equals(Double.NaN) || o.getHigh().equals(Double.NaN) ||
					o.getLow().equals(Double.NaN) 
					|| o.getClose().equals(Double.NaN))
				return; 
			writer.write(o.getMdiId(), o.getTimeStamp(), "OPEN", o.getOpen());
			writer.write(o.getMdiId(), o.getTimeStamp(), "HIGH", o.getHigh());
			writer.write(o.getMdiId(), o.getTimeStamp(), "LOW", o.getLow());
			writer.write(o.getMdiId(), o.getTimeStamp(), "CLOSE", o.getClose());
			if(o.getVolume()!=null)writer.write(o.getMdiId(), o.getTimeStamp(), "VOLUME",o.getVolume());
		}

	}

	public CandleRecorder(IArchiveFactory archFac, ITransportFactory transFac,
			TimeFrame tf, String mdiFile) throws Exception {
		super("CandleRecorder " + tf, transFac);
		this.tf = tf;
		this.archiveFactory = archFac;
		this.transFac = transFac;
		writer = archiveFactory.getWriter(tf);

		subscribe(mdiFile);
		t.schedule(
				new InternalTimerTask(),
				(collectionPhase - System.currentTimeMillis() % collectionPhase));

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
			OHLCV temp = new OHLCV();
			temp.setResolutionInSeconds(tf.getMinutes() * 60);
			temp.setMdiId(s);
			transFac.getReceiver(temp.getId()).getRawEvent()
					.addEventListener(new IEventListener<byte[]>() {
						@Override
						public void eventFired(byte[] event) {
							BaseMessage bm;
							try {
								bm = marshaller.demarshall(event);
								if (log.isDebugEnabled())
									log.debug("Event type: " + bm.getType());
								if (bm.getType().equals(CommandType.OHLC)) {
									OHLCV o = marshaller.demarshall((AQMessages.OHLC) bm
													.getExtension(AQMessages.OHLC.cmd));
									
									o.setResolutionInSeconds(tf.getMinutes() * 60);
									collectionList.add(o);
								}
							} catch (Exception e) {
								e.printStackTrace();
								log.warn("Exception: ", e);
							}

						}
					});

		}
	}

	@Override
	public String getDescription() {
		return "Candle recorder for timeframe " + tf;
	}

}
