package com.activequant.backtesting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.IArchiveReader;
import com.activequant.archive.TimeSeriesIterator;
import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.exceptions.InvalidDate8Time6Input;
import com.activequant.tools.streaming.DoubleValStreamEvent;
import com.activequant.trading.ITradingSystem;
import com.activequant.trading.virtual.VirtualExchange;
import com.activequant.transport.ITransportFactory;
import com.activequant.transport.memory.InMemoryTransportFactory;
import com.activequant.utils.Date8Time6Parser;

public class Backtester {

	private String[] marketDataInstrumentIds;
	private IArchiveReader archiveReader;
	private IMarketDataInstrumentDao mdiDao;
	private IInstrumentDao instrumentDao;
	private TimeStamp startTime, endTime;
	private String[] fields;
	private VirtualExchange vex = new VirtualExchange();
	private ITransportFactory transportFactory = new InMemoryTransportFactory();
	
	
	public Backtester(String[] mdis, String timeFrameString, String[] fields,
			IArchiveFactory factory, ITradingSystem[] tradingSystems) throws InvalidDate8Time6Input {
		//
		this.marketDataInstrumentIds = mdis;
		this.archiveReader = factory.getReader(TimeFrame
				.valueOf(timeFrameString));
		//
		this.fields = fields;
		Date8Time6Parser p = new Date8Time6Parser();
		startTime = new TimeStamp(p.getNanoseconds(20000101000000.0));
		endTime = new TimeStamp(p.getNanoseconds(20120101000000.0));
		
		//
		
		
	}

	public void init() {
		
	}

	public void execute() throws Exception {
		int id = 0;
		List<TimeSeriesIterator> tempList = new ArrayList<TimeSeriesIterator>();
		// add the trading time stream. 
		tempList.add(new TradingTimeStream(startTime, endTime));
		
		
		//
		for (String s : marketDataInstrumentIds) {
			for (String f : fields) {
				TimeSeriesIterator iterator = archiveReader
						.getTimeSeriesStream(s, f, startTime, endTime);
				tempList.add(iterator);				
			}
		}
		
		FastStreamer fs = new FastStreamer(tempList.toArray(new TimeSeriesIterator[]{}));
		
		
		
		long l1 = System.currentTimeMillis(); 
		long eventCount = 0; 
		// iterate over all data and feed it into the event bus. 		
		while(fs.moreDataInPipe()){			
			DoubleValStreamEvent chunk = fs.getOneFromPipes();	
			// System.out.println(chunk.getTimeStamp() + " --> " + chunk.getPayload());
			
			
			
			// vex.processStreamEvent(chunk);
			
			
			//
			eventCount ++; 
		}
		
		long l2 = System.currentTimeMillis();
		
		long difference = l2 - l1; 
		
		System.out.println("Replayed " + eventCount + " in " + difference + "ms. That's " + (eventCount/(double)difference) + " events/ms");
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		String initFile = "backtester.xml";
		if (args.length > 0)
			initFile = args[0];
		ApplicationContext appContext = new ClassPathXmlApplicationContext(
				initFile);
		Backtester bt = appContext.getBean("backtester", Backtester.class);
		bt.init();
		bt.execute();
	}

}
