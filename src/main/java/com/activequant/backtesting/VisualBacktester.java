package com.activequant.backtesting;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.activequant.aqviz.GlobalVizEvents;
import com.activequant.backtesting.reporting.PNLMonitor;
import com.activequant.domainmodel.AlgoConfig;
import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.backtesting.BacktestConfiguration;
import com.activequant.domainmodel.streaming.MarketDataEvent;
import com.activequant.domainmodel.streaming.ReferenceDataEvent;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.streaming.StreamEventIterator;
import com.activequant.domainmodel.streaming.TradingDataEvent;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.trading.IExchange;
import com.activequant.interfaces.trading.ITradingSystem;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.trading.TradingSystemEnvironment;
import com.activequant.trading.virtual.VirtualExchange;
import com.activequant.utils.TimeMeasurement;

/**
 * 
 * The visual backtester is a non-parallel backtester. If you want to speed up backtesting, for example by testing day-by-day or week-by-week in 
 * parallel, use the ParallelizedBacktester. 
 * 
 * Setting the interactive flag to false will result in no backtest interface. 
 * 
 * @author GhostRider
 * 
 */
public class VisualBacktester extends AbstractBacktester {

	private IExchange exchange;
	private ITransportFactory transportFactory;
	@SuppressWarnings("rawtypes")
	private StreamEventIterator[] streamIters;
	private ITradingSystem[] tradingSystems;
	private int tickPlayAmount = 0;
	private FastStreamer fs;
	long eventCount = 0;
	private JFrame jframe;
	private boolean runFlag = true;
	private boolean runUntilOrderEvent = false;
	private PNLMonitor pnlMonitor;
	private boolean interactive = true;
	private boolean sysExit = true; 
	private Logger log = Logger.getLogger(VisualBacktester.class);

	public VisualBacktester(IArchiveFactory factory, ITransportFactory transportFactory, IDaoFactory daoFactory,
			IExchange exchange, ITradingSystem[] tradingSystems, StreamEventIterator[] streamIters, BacktestConfiguration bc) throws Exception {
		this(factory, transportFactory, daoFactory, exchange, tradingSystems, streamIters, bc, true);
	}


	/**
	 * 
	 * @param factory
	 * @param transportFactory
	 * @param daoFactory
	 * @param exchange
	 * @param tradingSystems
	 * @param streamIters
	 * @param bc
	 * @param interactive set it to false to have no user interface. Use start to trigger the backtest. 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public VisualBacktester(IArchiveFactory factory, ITransportFactory transportFactory, IDaoFactory daoFactory,
			IExchange exchange, ITradingSystem[] tradingSystems, StreamEventIterator[] streamIters, BacktestConfiguration bc, boolean interactive)
			throws Exception {

		super(bc);
		//
		this.exchange = exchange;
		this.streamIters = streamIters;
		this.transportFactory = transportFactory;
		this.tradingSystems = tradingSystems;
		this.interactive = interactive;

		// add the order event listener
		if (exchange instanceof VirtualExchange) {
			((VirtualExchange) exchange).getGlobalOrderEvent().addEventListener(oelistener);
			((VirtualExchange) exchange).getGlobalOrderEvent().addEventListener(new IEventListener<OrderEvent>() {
				@Override
				public void eventFired(OrderEvent event) {
					if (runUntilOrderEvent)
						tickPlayAmount = 0;
				}
			});

		}
		pnlMonitor = new PNLMonitor(transportFactory);

		super.setPnlMonitor(pnlMonitor);

		// construct the trading system environment.
		TradingSystemEnvironment env = new TradingSystemEnvironment();
		env.setArchiveFactory(factory);
		env.setDaoFactory(daoFactory);
		env.setExchange(exchange);
		env.setTransportFactory(transportFactory);
		for (ITradingSystem s : tradingSystems) {
			s.environment(env);
		}

		for (ITradingSystem s : tradingSystems) {
			s.initialize();
		}

		fs = new FastStreamer(streamIters);

		TimeMeasurement.start("BACKTEST");
		log.info("Starting replay.");
		for (ITradingSystem s : tradingSystems) {
			s.start();
		}

		
		if (interactive) {

			jframe = new JFrame();
			jframe.setTitle("Market replay control tool");
			jframe.getContentPane().setLayout(new GridLayout(1, 6));

			JButton play = new JButton("Play");
			jframe.getContentPane().add(play);
			play.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tickPlayAmount = Integer.MAX_VALUE;
					runUntilOrderEvent = false;
				}
			});

			JButton pause = new JButton("Pause");
			pause.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tickPlayAmount = 0;
					runUntilOrderEvent = false;
				}
			});
			jframe.getContentPane().add(pause);

			JButton step = new JButton("1 step");
			step.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tickPlayAmount = 1;
					runUntilOrderEvent = false;
				}
			});

			jframe.getContentPane().add(step);

			JButton step50 = new JButton("50 steps");
			step50.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tickPlayAmount = 50;
					runUntilOrderEvent = false;
				}
			});
			jframe.getContentPane().add(step50);

			JButton runUntilExecutionButton = new JButton("Run to next execution");
			runUntilExecutionButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tickPlayAmount = Integer.MAX_VALUE;
					runUntilOrderEvent = true;
				}
			});
			jframe.getContentPane().add(runUntilExecutionButton);

			JButton stop = new JButton("Exit");
			stop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						stop();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});

			jframe.getContentPane().add(stop);
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jframe.setSize(600, 75);
			jframe.setVisible(true);
			jframe.toFront();
			// also show the live chart.
			pnlMonitor.showLiveChart();
			//
		}

	}

	public void stop() throws Exception {

		List<AlgoConfig> algoConfigs = new ArrayList<AlgoConfig>();
		for (ITradingSystem s : tradingSystems) {
			s.stop();
			algoConfigs.add(s.getAlgoConfig());
		}

		super.setAlgoConfigs(algoConfigs.toArray(new AlgoConfig[] {}));

		TimeMeasurement.stop("BACKTEST");

		long difference = TimeMeasurement.getRuntime("BACKTEST");
		log.info("Replayed " + eventCount + " events in " + difference + "ms. That's "
				+ (eventCount / (double) difference) + " events/ms");
		runFlag = false;

		// generate the report.
		super.generateReport();

		//
		GlobalVizEvents.getInstance().getEvent().fire("EXIT");

		if(sysExit)System.exit(0);
		

	}

	public void execute() throws Exception {

		while (runFlag) {

			// iterate over all data and feed it into the event bus.
			while (fs.moreDataInPipe() && (tickPlayAmount > 0 || !interactive)) {

				//
				StreamEvent se = fs.getOneFromPipes();
				ETransportType transportType = se.getEventType();

				// only time events are sent to the generic transport layer.
				if (transportType.equals(ETransportType.TIME)) {
					transportFactory.getPublisher(transportType.toString()).send(se);
				}

				// everything's a time event, so i also have to catch the
				// fine rest.
				if (transportType.equals(ETransportType.MARKET_DATA)) {
					exchange.processStreamEvent(se);
					MarketDataEvent mde = (MarketDataEvent) se;
					transportFactory.getPublisher(transportType, mde.getMdiId()).send(se);
					// send it also into our VIRTEX vortex
				} else if (transportType.equals(ETransportType.REF_DATA)) {
					ReferenceDataEvent rde = (ReferenceDataEvent) se;
					transportFactory.getPublisher(transportType, rde.getInstrument()).send(se);
				} else if (transportType.equals(ETransportType.TRAD_DATA)) {
					exchange.processStreamEvent(se);
					TradingDataEvent tde = (TradingDataEvent) se;
					transportFactory.getPublisher(transportType, tde.getTradInstId()).send(se);
					// send everything also to virtex exchange layer.
				} else {
					// push it out over its ID.
					transportFactory.getPublisher(se.getId()).send(se);
				}

				//
				eventCount++;
				tickPlayAmount--;
			}
			// checking every 100ms if we are to replay more. This delay does
			// not impact the backtesting performance.
			Thread.sleep(50);
			// check if there would still be more.
			if (!fs.moreDataInPipe())
				runFlag = false;
		}
		if (!interactive)
			stop();
	}

	public boolean isSysExit() {
		return sysExit;
	}

	public void setSysExit(boolean sysExit) {
		this.sysExit = sysExit;
	}

}
