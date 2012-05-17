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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.IArchiveWriter;
import com.activequant.dao.IDaoFactory;
import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.exceptions.TransportException;
import com.activequant.transport.ETransportType;
import com.activequant.transport.ITransportFactory;
import com.activequant.utils.events.IEventListener;

/**
 * A market data snapshot recorder. 
 * 
 * @author GhostRider
 *
 */
public class CandleRecorder {
	
	private IArchiveFactory archiveFactory;
	private ITransportFactory  transFac;
	private Logger log = Logger.getLogger(CandleRecorder.class);
	final Timer t = new Timer(true);
	final long collectionPhase = 5000l;
	private TimeFrame tf; 
	private final ConcurrentLinkedQueue<OHLCV> collectionList = new ConcurrentLinkedQueue<OHLCV>();
	
	private final IArchiveWriter writer; 
	
	class InternalTimerTask extends TimerTask{
		@Override
		public void run() {
			Object o = collectionList.poll();
			int counter = 0; 
			while(o!=null){
				counter ++; 
				store((OHLCV)o);
				o = collectionList.poll();				
			}
			log.info("Collected " + counter + " events. " );
			if(counter>0){
				try {
					writer.commit();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			t.schedule(new InternalTimerTask() , (collectionPhase - System.currentTimeMillis()%collectionPhase));				
		}
		
		public void store(OHLCV mds){
			if(mds==null)return;
			writer.write(mds.getMdiId(), mds.getTimeStamp(), "OPEN", mds.getOpen());
			writer.write(mds.getMdiId(), mds.getTimeStamp(), "HIGH", mds.getHigh());
			writer.write(mds.getMdiId(), mds.getTimeStamp(), "LOW", mds.getLow());
			writer.write(mds.getMdiId(), mds.getTimeStamp(), "CLOSE", mds.getClose());
			writer.write(mds.getMdiId(), mds.getTimeStamp(), "VOLUME", mds.getVolume());				
		}
		
	}
	
	public CandleRecorder(String springFile, String mdiFile, TimeFrame tf) throws IOException, TransportException{
		
		this.tf = tf; 
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{springFile});
		System.out.println("Starting up and fetching idf");
		IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
		archiveFactory = (IArchiveFactory) appContext.getBean("archiveFactory");
		
		writer = archiveFactory.getWriter(tf);
		transFac = appContext.getBean("jmsTransport", ITransportFactory.class);		
		subscribe(mdiFile);
		t.schedule(new InternalTimerTask(), (collectionPhase - System.currentTimeMillis()%collectionPhase));
		
	}
	
	
	private void subscribe(String mdiFile) throws IOException, TransportException{
		
		List<String> instruments = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mdiFile)));
		String l = br.readLine();
		while (l != null) {
			if (!l.startsWith("#") && !l.isEmpty()) {
				String symbol = l;
				int depth = 1; 
				if(l.indexOf(";")!=-1){
					String[] s = l.split(";");
					symbol = s[0];
					depth = Integer.parseInt(s[1]);
				}
				instruments.add(symbol);
			}
			l = br.readLine();
		}		
		for(String s : instruments){
			OHLCV temp = new OHLCV();
			temp.setResolutionInSeconds(tf.getMinutes()*60);
			temp.setMdiId(s);
			transFac.getReceiver(temp.getId()).getMsgRecEvent().addEventListener(new IEventListener<PersistentEntity>() {
				@Override
				public void eventFired(PersistentEntity event) {
					if(event instanceof OHLCV){
						collectionList.add((OHLCV)event);
					}
				}
			});
		}		
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws TransportException 
	 */
	public static void main(String[] args) throws IOException, TransportException {
		new CandleRecorder(args[0], args[1], TimeFrame.valueOf(args[2]));
	}

}
