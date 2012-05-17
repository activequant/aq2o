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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveFactory;
import com.activequant.dao.IDaoFactory;
import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.exceptions.TransportException;
import com.activequant.tools.streaming.MarketDataSnapshot;
import com.activequant.transport.ETransportType;
import com.activequant.transport.ITransportFactory;
import com.activequant.utils.events.IEventListener;

/**
 * A chandler makes candles.  
 * 
 * @author GhostRider
 *
 */
public class Chandler {
	
	private ITransportFactory  transFac;
	private Logger log = Logger.getLogger(Chandler.class);
	final Timer t = new Timer(true);
	final int timeFrameInMs;
	
	
	class InternalTimerTask extends TimerTask{
		@Override
		public void run() {
			List<OHLCV> candleList= new ArrayList<OHLCV>();
			synchronized (candles) {
				Iterator<Entry<String, OHLCV>> it =candles.entrySet().iterator();
				while(it.hasNext()){
					OHLCV o = it.next().getValue();
					candleList.add(o.clone());
					o.clear();					
				}
			}
			process(candleList);
			log.info("Drew " + candleList.size() + ". " );
			t.schedule(new InternalTimerTask() , (timeFrameInMs - System.currentTimeMillis()%timeFrameInMs));				
		}
		
		public void process(List<OHLCV> candleList){
			for(OHLCV o : candleList){
				if(o.getOpen()!=null){
					System.out.println(o.getMdiId()+ " - " + o.getOpen() + " - " + o.getClose());
					try {
						transFac.getPublisher(o.getId()).send(o);
					} catch (TransportException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}		
	}
	
	public Chandler(String springFile, String mdiFile, TimeFrame tf) throws IOException, TransportException{
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{springFile});
		this.timeFrameInMs = tf.getMinutes()* 60 * 1000;
		System.out.println("Starting up and fetching idf");
		transFac = appContext.getBean("jmsTransport", ITransportFactory.class);		
		subscribe(mdiFile);
		t.schedule(new InternalTimerTask(), (timeFrameInMs - System.currentTimeMillis()%timeFrameInMs));
		
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
			transFac.getReceiver(ETransportType.MARKET_DATA, s).getMsgRecEvent().addEventListener(new IEventListener<PersistentEntity>() {
				@Override
				public void eventFired(PersistentEntity event) {
					if(event instanceof MarketDataSnapshot){
						process((MarketDataSnapshot)event);
					}
				}
			});
		}		
	}
	
	private Map<String, OHLCV> candles = new HashMap<String, OHLCV>();
	
	private OHLCV getCandle(String mdiId){
		OHLCV ret = candles.get(mdiId);
		if(ret==null){
			ret = new OHLCV();
			ret.setResolutionInSeconds(timeFrameInMs/1000);
			ret.setMdiId(mdiId);
			synchronized (candles) {
				candles.put(mdiId, ret);	
			}			
		}
		return ret; 
	}
	
	protected void process(MarketDataSnapshot mds){
		if(mds==null)return;
		String seriesId =mds.getMdiId();
		Double bid = null;
		Double ask = null;
		// 
		if(mds.getBidSizes()!=null && mds.getBidSizes().length>0){
			double bestBidPx = mds.getBidPrices()[0];
			double bestBidQ = mds.getBidSizes()[0];
			bid = bestBidPx;
		}
		if(mds.getAskSizes()!=null && mds.getAskSizes().length>0){
			double bestAskPx = mds.getAskPrices()[0];
			double bestAskQ = mds.getAskSizes()[0];
			ask = bestAskPx;
		}
		// 
		if(bid!=null && ask!=null){
			double mid = (bid+ask)/2.0;
			OHLCV o = getCandle(seriesId);	
			o.update(mds.getTimeStamp(), mid);
		}		
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws TransportException 
	 */
	public static void main(String[] args) throws IOException, TransportException {
		new Chandler(args[0], args[1], TimeFrame.valueOf(args[2]));
	}

}
