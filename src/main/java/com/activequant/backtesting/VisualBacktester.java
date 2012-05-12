package com.activequant.backtesting;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.activequant.aqviz.GlobalVizEvents;
import com.activequant.archive.IArchiveFactory;
import com.activequant.backtesting.reporting.PNLMonitor;
import com.activequant.dao.IDaoFactory;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.tools.streaming.MarketDataEvent;
import com.activequant.tools.streaming.ReferenceDataEvent;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.tools.streaming.StreamEventIterator;
import com.activequant.tools.streaming.TradingDataEvent;
import com.activequant.trading.ITradingSystem;
import com.activequant.trading.TradingSystemEnvironment;
import com.activequant.trading.virtual.IExchange;
import com.activequant.trading.virtual.VirtualExchange;
import com.activequant.transport.ETransportType;
import com.activequant.transport.ITransportFactory;
import com.activequant.utils.TimeMeasurement;
import com.activequant.utils.events.IEventListener;

public class VisualBacktester extends AbstractBacktester {

	private IExchange exchange;
	private ITransportFactory transportFactory;
	@SuppressWarnings("rawtypes")
	private StreamEventIterator[] streamIters;
	private ITradingSystem[] tradingSystems;
	private int tickPlayAmount = 0;
	private FastStreamer fs;
	long eventCount = 0;
	private JFrame jframe = new JFrame();
	private boolean runFlag = true;
	private boolean runUntilOrderEvent = false;
	private PNLMonitor pnlMonitor;

	@SuppressWarnings("rawtypes")
	public VisualBacktester(IArchiveFactory factory, ITransportFactory transportFactory, IDaoFactory daoFactory,
			IExchange exchange, ITradingSystem[] tradingSystems, StreamEventIterator[] streamIters) throws Exception {

		//
		this.exchange = exchange;
		this.streamIters = streamIters;
		this.transportFactory = transportFactory;
		this.tradingSystems = tradingSystems;

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
		pnlMonitor.showLiveChart();

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

		for (ITradingSystem s : tradingSystems) {
			s.start();
		}

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

		//

	}

	public void stop() throws Exception {

		for (ITradingSystem s : tradingSystems) {
			s.stop();
		}

		TimeMeasurement.stop("BACKTEST");

		long difference = TimeMeasurement.getRuntime("BACKTEST");
		System.out.println("Replayed " + eventCount + " events in " + difference + "ms. That's "
				+ (eventCount / (double) difference) + " events/ms");
		runFlag = false;

		// generate the report.
		super.generateReport();

		//
		GlobalVizEvents.getInstance().getEvent().fire("EXIT");

		System.exit(0);
		;

	}

	public void execute() throws Exception {

		while (runFlag) {

			// iterate over all data and feed it into the event bus.
			while (fs.moreDataInPipe() && tickPlayAmount > 0) {

				//
				StreamEvent se = fs.getOneFromPipes();
				ETransportType transportType = se.getEventType();

				// only time events are sent to the generic transport layer.
				if (transportType.equals(ETransportType.TIME)) {
					transportFactory.getPublisher(transportType.toString()).send(se);
				}

				// everything's a time event, so i also have to catch the
				// fucking rest.
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
				}

				//
				eventCount++;
				tickPlayAmount--;
			}

			// checking every 100ms if we are to replay more. This delay does
			// not impact the backtesting performance.
			Thread.sleep(50);
		}
	}

}
