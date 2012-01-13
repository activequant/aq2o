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
import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.exceptions.InvalidDate8Time6Input;
import com.activequant.tools.streaming.DoubleValStreamEvent;
import com.activequant.trading.virtual.IExchange;
import com.activequant.trading.virtual.VirtualExchange;

public class Backtester {

	private String[] marketDataInstrumentIds;
	private IArchiveReader archiveReader;
	private IMarketDataInstrumentDao mdiDao;
	private IInstrumentDao instrumentDao;
	private Date8Time6 startTime, endTime;
	private String[] fields;
	private IExchange vex = new VirtualExchange();
	
	
	public Backtester(String[] mdis, String timeFrameString, String[] fields,
			IArchiveFactory factory) throws InvalidDate8Time6Input {
		//
		this.marketDataInstrumentIds = mdis;
		this.archiveReader = factory.getReader(TimeFrame
				.valueOf(timeFrameString));
		//
		this.fields = fields;
		startTime = new Date8Time6(20000101000000.0);
		endTime = new Date8Time6(20120101000000.0);
	}

	public void init() {
		
	}

	public void execute() throws Exception {
		int id = 0;
		List<TimeSeriesIterator> tempList = new ArrayList<TimeSeriesIterator>();
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
