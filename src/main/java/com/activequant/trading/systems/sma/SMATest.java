package com.activequant.trading.systems.sma;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.csv.CsvArchiveReaderFormat1;
import com.activequant.backtesting.BacktestConfiguration;
import com.activequant.backtesting.FieldToBidAskConverterStream;
import com.activequant.backtesting.VisualBacktester;
import com.activequant.dao.IDaoFactory;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.tools.streaming.StreamEventIterator;
import com.activequant.trading.ITradingSystem;
import com.activequant.trading.virtual.VirtualExchange;
import com.activequant.transport.ITransportFactory;
import com.activequant.transport.memory.InMemoryTransportFactory;
import com.activequant.utils.Date8Time6Parser;

/**
 * 
 * @author GhostRider
 *
 */
public class SMATest {

	/**
	 * will be moved soon to AbstractBacktester
	 * @author GhostRider
	 *
	 */
	private class BasicBacktestEnv {

		

		public void backtest(BacktestConfiguration bc, ITradingSystem[] its, List<StreamEventIterator> listOfStreams) throws Exception {
			//
			//
			//
			ApplicationContext appContext = new ClassPathXmlApplicationContext("fwspring.xml");
			IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
			IArchiveFactory archiveFactory = (IArchiveFactory) appContext.getBean("archiveFactory");
			//
			//
			// initialize transport layer and VirtEX
			ITransportFactory transport = new InMemoryTransportFactory();
			VirtualExchange virtEx = new VirtualExchange(transport);
			//
			//
			// initialize the backtester
			VisualBacktester bt = new VisualBacktester(archiveFactory, transport, idf, virtEx, its,
					listOfStreams.toArray(new StreamEventIterator[] {}), bc);
			// set the backtest config, for later reporting. 
			
			//
			//
			// ok, now that we have all initialized ... execute the backtest.
			bt.execute();
			//
		}
		
	}

	private long date8Time6Start = 20000101000000l;
	private long date8Time6End = 20130101000000l; 
	
	public SMATest() throws Exception {
		Date8Time6Parser p = new Date8Time6Parser();
		TimeStamp startTime = new TimeStamp(p.getNanoseconds(date8Time6Start));
		TimeStamp endTime = new TimeStamp(p.getNanoseconds(date8Time6End));

		BacktestConfiguration btCfg = new BacktestConfiguration();
		btCfg.setBacktesterImplementation(VisualBacktester.class.getCanonicalName());
		btCfg.setDate8Time6Start(date8Time6Start);
		btCfg.setDate8Time6End(date8Time6End);
		btCfg.setResolutionTimeFrame(TimeFrame.MINUTES_1.name());
		
		
		MarketDataInstrument mdi = new MarketDataInstrument("CSV", "SOY");
		TradeableInstrument tdi = new TradeableInstrument("CSV", "SOY");

		
		btCfg.setMdis(new String[]{mdi.getId()});
		btCfg.setTdis(new String[]{tdi.getId()});
		
		
		// construct the stream list
		@SuppressWarnings("rawtypes")
		List<StreamEventIterator> tempList = new ArrayList<StreamEventIterator>();
		// add the trading time stream, one interval every hour.
		// tempList.add(new TradingTimeStreamIterator(startTime, endTime, 60L *
		// 1000l * 1000l * 1000l * 60l));

		
		tempList.add(new FieldToBidAskConverterStream(btCfg.getMdis()[0], btCfg.getTdis()[0], "PX_SETTLE", startTime, endTime,
				new CsvArchiveReaderFormat1("./src/test/resources/sampledata/soybean_future_rolled.csv")));

		SimpleMovingAverage ssts = new SimpleMovingAverage();
		ssts.setVizLayer(true);
		
		new BasicBacktestEnv().backtest(btCfg, new ITradingSystem[]{ssts}, tempList);

	}

	public static void main(String[] args) throws Exception {
		new SMATest();
	}
}
