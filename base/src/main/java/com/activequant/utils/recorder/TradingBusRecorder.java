package com.activequant.utils.recorder;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.IOrderEventDao;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.Marshaller;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * A recorder that records order events that come in over the trading bus. In
 * the near future, this might record more than just order events.
 * 
 * @author GhostRider
 * 
 */
public class TradingBusRecorder extends ComponentBase {

	private final ITransportFactory transFac;
	private final Logger log = Logger.getLogger(TradingBusRecorder.class);
	private final Timer t = new Timer(true);
	private final long collectionPhase = 5000l;
	private final ConcurrentLinkedQueue<OrderEvent> collectionList = new ConcurrentLinkedQueue<OrderEvent>();
	private final IOrderEventDao orderEventDao;

	class InternalTimerTask extends TimerTask {
		int counter;

		@Override
		public void run() {

			OrderEvent o = collectionList.poll();
			counter = 0;
			while (o != null) {
				try {
					orderEventDao.create(o);
				} catch (DaoException e) {
					log.error(o.toString() + " was not stored: ", e);
				}
				//
				counter++;
				o = collectionList.poll();
			}
			log.info("Collected " + counter + " events. ");
			t.schedule(new InternalTimerTask(),
					(collectionPhase - System.currentTimeMillis()
							% collectionPhase));
		}
	}

	public TradingBusRecorder(IDaoFactory daoF, ITransportFactory transFac)
			throws Exception {
		super("TradingBusRecorder", transFac);
		this.transFac = transFac;
		this.orderEventDao = daoF.orderEventDao();
		log.info("Transport initialized.");
		subscribe();
		t.schedule(
				new InternalTimerTask(),
				(collectionPhase - System.currentTimeMillis() % collectionPhase));
	}

	private Marshaller marshaller = new Marshaller();

	private void subscribe() throws IOException, TransportException {
		log.info("Subscribing to all order events on the TRAD_DATA bus.");
		transFac.getReceiver(ETransportType.TRAD_DATA, "*").getRawEvent()
				.addEventListener(new IEventListener<byte[]>() {
					@Override
					public void eventFired(byte[] event) {

						BaseMessage bm;
						try {
							bm = marshaller.demarshall(event);
							OrderEvent o = marshaller.demarshallOrderEvent(bm);
							if (o != null)
								collectionList.add(o);
						} catch (InvalidProtocolBufferException e) {
							e.printStackTrace();
						}

					}
				});
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				new String[] { args[0] });
		IDaoFactory idf = (IDaoFactory) appContext.getBean(IDaoFactory.class);
		ITransportFactory transFac = appContext
				.getBean(ITransportFactory.class);
		new TradingBusRecorder(idf, transFac);
	}

	@Override
	public String getDescription() {
		return "Records all order events to persistence layer. Writes in five second intervals. No supported commands. ";
	}

}
