package com.brownchipmunk;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.csv.CsvArchiveReaderFormat1;
import com.activequant.backtesting.FieldToBidAskConverterStream;
import com.activequant.backtesting.VisualBacktester;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.backtesting.BacktestConfiguration;
import com.activequant.domainmodel.streaming.StreamEventIterator;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.trading.ITradingSystem;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.trading.virtual.VirtualExchange;
import com.activequant.transport.memory.InMemoryTransportFactory;
import com.activequant.utils.Date8Time6Parser;

/**
 * 
 * @author GhostRider
 * 
 */
public class DetTest {

	/**
	 * will be moved soon to AbstractBacktester
	 * 
	 * @author GhostRider
	 * 
	 */
	private class BasicBacktestEnv {

		public void backtest(BacktestConfiguration bc, ITradingSystem[] its,
				List<StreamEventIterator> listOfStreams) throws Exception {
			//
			//
			//
			ApplicationContext appContext = new ClassPathXmlApplicationContext(
					"fwspring.xml");
			IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
			IArchiveFactory archiveFactory = null; // (IArchiveFactory) appContext
					// .getBean("archiveFactory");
			//
			//
			// initialize transport layer and VirtEX
			ITransportFactory transport = new InMemoryTransportFactory();
			VirtualExchange virtEx = new VirtualExchange(transport);
			//
			//
			// initialize the backtester
			VisualBacktester bt = new VisualBacktester(archiveFactory,
					transport, idf, virtEx, its,
					listOfStreams.toArray(new StreamEventIterator[] {}), bc);
			// set the backtest config, for later reporting.

			System.out.println();

			//
			//
			// ok, now that we have all initialized ... execute the backtest.
			bt.execute();
			//
		}
	}

	private long date8Time6Start = 19990101000000l;
	private long date8Time6End = 20000110000000l;

	public DetTest() throws Exception {
		Date8Time6Parser p = new Date8Time6Parser();
		TimeStamp startTime = new TimeStamp(p.getNanoseconds(date8Time6Start));
		TimeStamp endTime = new TimeStamp(p.getNanoseconds(date8Time6End));

		BacktestConfiguration btCfg = new BacktestConfiguration();
		btCfg.setBacktesterImplementation(VisualBacktester.class
				.getCanonicalName());
		btCfg.setDate8Time6Start(date8Time6Start);
		btCfg.setDate8Time6End(date8Time6End);
		btCfg.setResolutionTimeFrame(TimeFrame.MINUTES_1.name());

		MarketDataInstrument mdi = new MarketDataInstrument("CSV", "EURUSD");
		TradeableInstrument tdi = new TradeableInstrument("CSV", "EURUSD");

		btCfg.setMdis(new String[] { mdi.getId() });
		btCfg.setTdis(new String[] { tdi.getId() });

		// construct the stream list
		@SuppressWarnings("rawtypes")
		List<StreamEventIterator> tempList = new ArrayList<StreamEventIterator>();

		tempList.add(new FieldToBidAskConverterStream(btCfg.getMdis()[0], btCfg
				.getTdis()[0], "PX_SETTLE", startTime, endTime,
				new CsvArchiveReaderFormat1(
						"./src/test/resources/sampledata/Det.csv")));

		SimpleDeterministicStrategy ssts = new SimpleDeterministicStrategy();
		ssts.setVizLayer(true);

		new BasicBacktestEnv().backtest(btCfg, new ITradingSystem[] { ssts },
				tempList);
	}

	public static void main(String[] args) throws Exception {
		new DetTest();
	}
}
