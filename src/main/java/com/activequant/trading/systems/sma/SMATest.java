package com.activequant.trading.systems.sma;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.csv.CsvArchiveReaderFormat1;
import com.activequant.backtesting.FieldToBidAskConverterStream;
import com.activequant.backtesting.TradingTimeStreamIterator;
import com.activequant.backtesting.VisualBacktester;
import com.activequant.dao.IDaoFactory;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.tools.streaming.StreamEventIterator;
import com.activequant.trading.ITradingSystem;
import com.activequant.trading.virtual.VirtualExchange;
import com.activequant.transport.ITransportFactory;
import com.activequant.transport.memory.InMemoryTransportFactory;
import com.activequant.utils.Date8Time6Parser;

public class SMATest {
	public SMATest() throws Exception {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("fwspring.xml");
		System.out.println("Starting up and fetching idf");
		IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
		IArchiveFactory archiveFactory = (IArchiveFactory) appContext.getBean("archiveFactory");
		System.out.println("Fetched.");
		Date8Time6Parser p = new Date8Time6Parser();
		TimeStamp startTime = new TimeStamp(p.getNanoseconds(20000101000000.0));
		TimeStamp endTime = new TimeStamp(p.getNanoseconds(20111205000000.0));
		// TimeStamp endTime = new
		// TimeStamp(p.getNanoseconds(20120101000000.0));

		// construct the stream list
		@SuppressWarnings("rawtypes")
		List<StreamEventIterator> tempList = new ArrayList<StreamEventIterator>();
		// add the trading time stream, one interval every minute.
		
		//tempList.add(new TradingTimeStreamIterator(startTime, endTime, 60L * 1000l * 1000l * 1000l * 60l));

		SimpleMovingAverage ssts = new SimpleMovingAverage();
		
		MarketDataInstrument mdi = new MarketDataInstrument("CSV", "SOY");		
		TradeableInstrument tdi = new TradeableInstrument("CSV", "SOY");
		
		tempList.add(new FieldToBidAskConverterStream(mdi.getId(),tdi.getId(), "PX_SETTLE",startTime,endTime,
				new CsvArchiveReaderFormat1("./src/test/resources/sampledata/soybean_future_rolled.csv")));

		// tempList.add(new ArchiveStreamToMarketDataIterator(ssts.mdiId2,
		// ssts.tdiId2, startTime, endTime, archiveFactory
		// .getReader(TimeFrame.RAW)));

		ITransportFactory transport = new InMemoryTransportFactory();

		// instantiate a virtual exchange, which we will inject.
		VirtualExchange virtEx = new VirtualExchange(transport);
		//
		ssts.setVizLayer(true);

		//
		// initialize the backtester
		VisualBacktester bt = new VisualBacktester(archiveFactory, transport, idf, virtEx,
				new ITradingSystem[] { ssts }, tempList.toArray(new StreamEventIterator[] {}));
		// ok, now that we have all initialized ...

		// execute the backtest.
		bt.execute();

	}

	public static void main(String[] args) throws Exception {
		new SMATest();
	}
}
