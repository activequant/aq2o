package com.activequant.utils.recorder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.dao.IInstrumentDao;
import com.activequant.interfaces.dao.IMarketDataInstrumentDao;
import com.activequant.interfaces.dao.ITradeableInstrumentDao;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;

/**
 * 
 * @author GhostRider
 * 
 */
public class RefDataRecorder extends ComponentBase {

	private final ConcurrentLinkedQueue<byte[]> collectionList = new ConcurrentLinkedQueue<byte[]>();
	final Timer t = new Timer(true);
	final long collectionPhase = 5000l;
	// 
	private IMarketDataInstrumentDao mdiDao; 
	private ITradeableInstrumentDao tdiDao; 
	private IInstrumentDao iDao; 

	// 
	class InternalTimerTask extends TimerTask {
		int counter;

		@Override
		public void run() {
			Object o = collectionList.peek();
			counter = 0;
			while (o != null) {
				// work out our byte [] with ref data. 
				if(o instanceof byte[]){
					String s = new String((byte[])o);
					if(s.startsWith("SET")){
						// ok, let's parse it. 
						String[] parts = s.split(";");
						String type = parts[1];
						String id = parts[2];
						String field = parts[3];
						String value = parts[4];
						try {
							store(type, id, field, value);
							// storing worked out, so let's poll it. 
							collectionList.poll();
						} catch (DaoException e) {
							e.printStackTrace();
						}
					}
				}
				o = collectionList.poll();
			}
		}		
		
		private void store(String type, String id, String field, String value) throws DaoException{
			// 
			if(type.equals("MDI")){
				// 
				
			}
			else if(type.equals("TDI")){
				// 
			}
			else if(type.equals("IID")){
				// ok. plain instrument. 
				Instrument i = iDao.load(id);
				i.getUnderlyingMap().put(field, value);
				// 
				// 
			}
			else if(type.equals("GEN")){
				// generic reference data. 
				
				
				// 
			}
			// 
		}
	}
	
	/**
	 * Use this constructor to have Spring's autowiring kick in ..
	 * 
	 * @param transFac
	 * @throws Exception
	 */
	public RefDataRecorder(ITransportFactory transFac) throws Exception {
		super("RefDataRecorder", transFac);
		init();
	}

	private void init() throws TransportException {
		// subscribe to the generic ref data bus.
		IReceiver r = transFac.getReceiver(ETransportType.REF_DATA.toString());

		// register event handle
		r.getRawEvent().addEventListener(new IEventListener<byte[]>() {
			@Override
			public void eventFired(byte[] event) {
				collectionList.add(event);
			}
		});
		t.schedule(
				new InternalTimerTask(),
				(collectionPhase - System.currentTimeMillis() % collectionPhase));
	}

	@Override
	public String getDescription() {
		return "The ref data recorder subscribes to reference data and stores it to the database.";
	}

}
