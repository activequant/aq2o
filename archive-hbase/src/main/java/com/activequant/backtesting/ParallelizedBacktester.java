package com.activequant.backtesting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.domainmodel.backtesting.BacktestConfiguration;
import com.activequant.domainmodel.backtesting.SimulationReport;
import com.activequant.domainmodel.backtesting.TimeSetup;
import com.activequant.domainmodel.streaming.StreamEventIterator;
import com.activequant.domainmodel.streaming.TimeStreamEvent;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.backtesting.IStreamFactory;
import com.activequant.interfaces.backtesting.ITimeRangeSplitter;
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
		int usedCores = 1; // Runtime.getRuntime().availableProcessors() - 1;
		log.info("Instantiating a parallelized backtester with " + usedCores + " threads. ");
		threadPool = Executors.newFixedThreadPool(usedCores);

		Date8Time6Parser dtp = new Date8Time6Parser();
		List<TimeSetup> chunks = timeRangeSplitter.split(dtp.fromLong(outerBacktestConfig.getDate8Time6Start()),
				dtp.fromLong(outerBacktestConfig.getDate8Time6End()));


		//
		Set<Future<SimulationReport>> futures = new HashSet<Future<SimulationReport>>();

		for (int i = 0; i < chunks.size(); i++) {
			TimeSetup chunk = chunks.get(i);
			log.info("Obtained a chunk: " + chunk.toString());
			BacktestConfiguration localConfig = btConfig.clone();
			localConfig.setTimeSetup(chunk);
			//
			BacktestJob job = new BacktestJob(aqAppContextSpringFile, localConfig);
			Future<SimulationReport> f = threadPool.submit(job);
			futures.add(f);
		}

		//
		//
		List<SimulationReport> simReports = new ArrayList<SimulationReport>();
		for(Future<SimulationReport> f : futures)
			simReports.add(f.get());
		
		//
		threadPool.shutdown();
		log.info("All jobs done. Merging minor simulation reports into major report.");

		// need to merge PNL curves and transactions ...
		// more advanced statistics can be generated through the
		// transaction-list-2-report generator
		
		// 
		
		

	}

	class BacktestJob implements Callable<SimulationReport> {

		private BacktestConfiguration btConfig;
		private String aqAppContextSpringFile;

		BacktestJob(String aqAppContextSpringFile, BacktestConfiguration btConfig) {
			this.btConfig = btConfig;
			this.aqAppContextSpringFile = aqAppContextSpringFile;
		}

		@Override
		public SimulationReport call() {

			log.info("Simulating " + btConfig.getTimeSetup() + " with algo config: " + btConfig.getAlgoConfig());
			SimulationReport sr = new SimulationReport ();

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
				bt.setSysExit(false);
				bt.execute();
				sr = bt.stop();

				sr.setSimulationStatus("SUCCESS");
				

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

