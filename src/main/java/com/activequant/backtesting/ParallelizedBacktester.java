package com.activequant.backtesting;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.backtesting.reporting.HTMLReportGen;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.backtesting.BacktestConfiguration;
import com.activequant.domainmodel.backtesting.SimulationReport;
import com.activequant.domainmodel.backtesting.TimeSetup;
import com.activequant.domainmodel.streaming.StreamEventIterator;
import com.activequant.domainmodel.streaming.TimeStreamEvent;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.backtesting.IReportRenderer;
import com.activequant.interfaces.backtesting.IStreamFactory;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.trading.ITradingSystem;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.trading.virtual.VirtualExchange;
import com.activequant.transport.memory.InMemoryTransportFactory;
import com.activequant.utils.Date8Time6Parser;

/**
 * The parallelized backtester splits a time frame into smaller chunks and tests
 * these in parallel. Best suited for trading systems that liquidate at end of
 * day or end of week or end of month as no knowledge about what the different
 * chunks is exchanged between chunks.
 * 
 * 
 * Creates a thread pool and hands the chunks to the thread pool for
 * backtesting.
 * 
 * It uses an ITimeRangeSplitter to obtain a list of backtest chunks.
 * 
 * The individual outputs of these chunks are merged into a bigger report.
 * 
 * Spawns non-interactive visual backtesters.
 * 
 * Generates reports per backtest slice and stores these into a separate folder.
 * Finally aggregates the separate reports.
 * 
 * @author GhostRider
 * 
 */
public class ParallelizedBacktester extends AbstractBacktester {

	private ExecutorService threadPool;
	private Logger log = Logger.getLogger(ParallelizedBacktester.class);

	/**
	 * 
	 * 
	 * 
	 * @param factory
	 * @param transportFactory
	 * @param daoFactory
	 * @param exchange
	 * @param tradingSystems
	 * @param streamIters
	 * @param bc
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public ParallelizedBacktester(String aqAppContextSpringFile, BacktestConfiguration outerBacktestConfig,
			ITimeRangeSplitter timeRangeSplitter) throws Exception {

		super(outerBacktestConfig);

		// create a thread pool with nCores-1 threads.
		int usedCores = Runtime.getRuntime().availableProcessors() - 1;
		log.info("Instantiating a parallelized backtester with " + usedCores + " threads. ");
		threadPool = Executors.newFixedThreadPool(usedCores);

		Date8Time6Parser dtp = new Date8Time6Parser();
		List<TimeSetup> chunks = timeRangeSplitter.split(dtp.fromLong(outerBacktestConfig.getDate8Time6Start()),
				dtp.fromLong(outerBacktestConfig.getDate8Time6End()));

		//
		String baseReportTgtFldr = "" + System.currentTimeMillis();

		//
		Set<Callable<SimulationReport>> jobs = new HashSet<Callable<SimulationReport>>();

		for (int i = 0; i < chunks.size(); i++) {
			TimeSetup chunk = chunks.get(i);
			log.info("Obtained a chunk: " + chunk.toString());
			BacktestConfiguration localConfig = btConfig.clone();
			localConfig.setTimeSetup(chunk);

			// I willingly refrain from using a report generator factory here.
			HTMLReportGen h = new HTMLReportGen(baseReportTgtFldr + File.separator + i, "templates");
			//
			BacktestJob job = new BacktestJob(aqAppContextSpringFile, localConfig, h);
			jobs.add(job);
		}

		//
		threadPool.invokeAll(jobs);
		threadPool.shutdown();
		log.info("All jobs done. Merging minor simulation reports into major report.");

		// need to merge PNL curves and transactions ...
		// more advanced statistics can be generated through the
		// transaction-list-2-report generator

	}

	class BacktestJob implements Callable<SimulationReport> {

		private BacktestConfiguration btConfig;
		private IReportRenderer reportGen;
		private String aqAppContextSpringFile;

		BacktestJob(String aqAppContextSpringFile, BacktestConfiguration btConfig, IReportRenderer reportGen) {
			this.btConfig = btConfig;
			this.reportGen = reportGen;
			this.aqAppContextSpringFile = aqAppContextSpringFile;
		}

		@Override
		public SimulationReport call() {

			log.info("Simulating " + btConfig.getTimeSetup() + " with algo config: " + btConfig.getAlgoConfig());
			SimulationReport sr = new SimulationReport();

			try {

				//
				ApplicationContext appContext = new ClassPathXmlApplicationContext(aqAppContextSpringFile);
				IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
				IArchiveFactory archiveFactory = (IArchiveFactory) appContext.getBean("archiveFactory");
				//
				//
				// initialize transport layer and VirtEX
				ITransportFactory transport = new InMemoryTransportFactory();
				VirtualExchange virtEx = new VirtualExchange(transport);
				//
				
				// 
				ITradingSystem tradSys = (ITradingSystem) appContext.getBean("tradingSystem");
				IStreamFactory streamFactory= (IStreamFactory) appContext.getBean("streamFactory");
				StreamEventIterator<? extends TimeStreamEvent>[] streamIters = streamFactory.getStreamIterators(btConfig);
				//
				// initialize the backtester
				VisualBacktester bt = new VisualBacktester(archiveFactory, transport, idf, virtEx, new ITradingSystem[]{tradSys}, streamIters,
						btConfig, false);
				// set the backtest config, for later reporting.

				//
				//
				// ok, now that we have all initialized ... execute the
				// backtest.
				bt.execute();

				sr.setSimulationStatus("SUCCESS");
				// create the simulation report.

				//
				

			} catch (Exception ex) {
				log.warn("Exception while running backtest job", ex);
				sr.setSimulationStatus("Error while running backtest: " + ex);
			}
			return sr;
		}
	}

	public static void main(String[] args) throws Exception {
		BacktestConfiguration bt = new BacktestConfiguration();
		bt.setMdis(new String[]{"CSV.SOY"});
		bt.setTdis(new String[]{"CSV.SOY"});
		bt.setDate8Time6Start(20120101000000L);
		bt.setDate8Time6End(20120501000000L);
		new ParallelizedBacktester("backtest1.xml", bt, new TimeRangeSplitterWeekly());
	}

}

interface ITimeRangeSplitter {
	public List<TimeSetup> split(TimeStamp start, TimeStamp end);
}

/**
 * Reimplement for YOUR trading algo. Based on the start timestamp, it will
 * generate chunks of seven days length, up to end. The last frame could be
 * shorted than seven days, depending on your end date.
 * 
 * @author GhostRider
 * 
 */
class TimeRangeSplitterWeekly implements ITimeRangeSplitter {

	public List<TimeSetup> split(TimeStamp start, TimeStamp end) {
		List<TimeSetup> ret = new ArrayList<TimeSetup>();

		while (start.isBefore(end)) {
			TimeStamp localStart = new TimeStamp(start.getNanoseconds());
			//
			Calendar endCal = GregorianCalendar.getInstance();
			endCal.setTime(start.getCalendar().getTime());
			endCal.add(Calendar.DATE, 7);
			TimeStamp localEnd = new TimeStamp(endCal.getTime());
			if (localEnd.isAfter(end))
				localEnd = end;
			TimeSetup setup = new TimeSetup();
			setup.dataReplayStart = localStart;
			setup.tradingStart = localStart;
			setup.tradingEnd = localEnd;
			setup.dataReplayEnd = localEnd;

			ret.add(setup);
			start = localEnd;

		}

		return ret;
	}

}