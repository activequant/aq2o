package com.activequant.backtesting;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.activequant.archive.IArchiveFactory;
import com.activequant.dao.IDaoFactory;
import com.activequant.tools.streaming.MarketDataEvent;
import com.activequant.tools.streaming.ReferenceDataEvent;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.tools.streaming.StreamEventIterator;
import com.activequant.tools.streaming.TradingDataEvent;
import com.activequant.trading.ITradingSystem;
import com.activequant.trading.TradingSystemEnvironment;
import com.activequant.trading.virtual.IExchange;
import com.activequant.transport.ETransportType;
import com.activequant.transport.ITransportFactory;
import com.activequant.utils.TimeMeasurement;

public class VisualBacktester extends JFrame {

	private IExchange exchange;
	private ITransportFactory transportFactory;
	@SuppressWarnings("rawtypes")
	private StreamEventIterator[] streamIters;
	private ITradingSystem[] tradingSystems;
	private int tickPlayAmount = 0;
	private FastStreamer fs;
	long eventCount = 0;

	@SuppressWarnings("rawtypes")
	public VisualBacktester(IArchiveFactory factory, ITransportFactory transportFactory, IDaoFactory daoFactory,
			IExchange exchange, ITradingSystem[] tradingSystems, StreamEventIterator[] streamIters) throws Exception {

		//
		this.exchange = exchange;
		this.streamIters = streamIters;
		this.transportFactory = transportFactory;
		this.streamIters = streamIters;
		this.tradingSystems = tradingSystems;

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

		super.setTitle("Market replay control tool");
		super.getContentPane().setLayout(new GridLayout(1, 4));

		JButton play = new JButton("Play");
		super.getContentPane().add(play);
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tickPlayAmount = Integer.MAX_VALUE;
			}
		});

		JButton pause = new JButton("Pause");
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tickPlayAmount = 0;
			}
		});
		super.getContentPane().add(pause);

		JButton step = new JButton("1 step");
		step.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tickPlayAmount = 1;
			}
		});

		super.getContentPane().add(step);
		
		JButton step50 = new JButton("50 steps");
		step50.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tickPlayAmount = 50;
			}
		});
		super.getContentPane().add(step50);
		
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		super.setSize(600,75);
		super.setVisible(true);
		super.toFront();

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

	}

	public void execute() throws Exception {

		while (true) {

			// iterate over all data and feed it into the event bus.
			while (fs.moreDataInPipe() && tickPlayAmount > 0) {

				//
				StreamEvent se = fs.getOneFromPipes();
				ETransportType transportType = se.getEventType();

				// only time events are sent to the generic transport layer.
				if (transportType.equals(ETransportType.TIME)) {
					transportFactory.getPublisher(transportType.toString()).send(se);
				} else if (transportType.equals(ETransportType.MARKET_DATA)) {
					MarketDataEvent mde = (MarketDataEvent) se;
					transportFactory.getPublisher(transportType, mde.getMdiId()).send(se);

					// send also to virtex exchange layer.
					exchange.processStreamEvent(se);
				} else if (transportType.equals(ETransportType.REF_DATA)) {
					ReferenceDataEvent rde = (ReferenceDataEvent) se;
					transportFactory.getPublisher(transportType, rde.getInstrument()).send(se);
				} else if (transportType.equals(ETransportType.TRAD_DATA)) {
					TradingDataEvent tde = (TradingDataEvent) se;
					transportFactory.getPublisher(transportType, tde.getTradInstId()).send(se);

					// send everything also to virtex exchange layer.
					exchange.processStreamEvent(se);
				}

				//
				eventCount++;
				tickPlayAmount--;
			}
			
			// checking every 100ms if we are to replay more. 
			Thread.sleep(50);
			
		}
	}

}
