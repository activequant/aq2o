package com.activequant.backtesting;

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

public class Backtester {

	private IExchange exchange;
	private ITransportFactory transportFactory;
	@SuppressWarnings("rawtypes")
	private StreamEventIterator[] streamIters;
	private ITradingSystem[] tradingSystems;

	@SuppressWarnings("rawtypes")
	public Backtester(IArchiveFactory factory,
			ITransportFactory transportFactory, IDaoFactory daoFactory,
			IExchange exchange, ITradingSystem[] tradingSystems,
			StreamEventIterator[] streamIters) throws Exception {

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

	}

	public void execute() throws Exception {

		@SuppressWarnings("unchecked")
		FastStreamer fs = new FastStreamer(streamIters);

		TimeMeasurement.start("BACKTEST");

		for (ITradingSystem s : tradingSystems) {
			s.start();
		}

		long eventCount = 0;
		// iterate over all data and feed it into the event bus.
		while (fs.moreDataInPipe()) {

			//
			StreamEvent se = fs.getOneFromPipes();
			ETransportType transportType = se.getEventType();

			// only time events are sent to the generic transport layer.
			if (transportType.equals(ETransportType.TIME)) {
				transportFactory.getPublisher(transportType.toString())
						.send(se);
			} else if (transportType.equals(ETransportType.MARKET_DATA)) {
				MarketDataEvent mde = (MarketDataEvent) se;
				transportFactory.getPublisher(transportType, mde.getMdiId()).send(se);
				
				// send also to virtex exchange layer.
				exchange.processStreamEvent(se);
			} else if (transportType.equals(ETransportType.REF_DATA)) {
				ReferenceDataEvent rde = (ReferenceDataEvent) se;
				transportFactory.getPublisher(transportType,
						rde.getInstrument()).send(se);
			} else if (transportType.equals(ETransportType.TRAD_DATA)) {
				TradingDataEvent tde = (TradingDataEvent) se;
				transportFactory.getPublisher(transportType, tde.getTradInst())
						.send(se);

				// send everything also to virtex exchange layer.
				exchange.processStreamEvent(se);
			}

			//
			eventCount++;
		}

		for (ITradingSystem s : tradingSystems) {
			s.stop();
		}

		TimeMeasurement.stop("BACKTEST");

		long difference = TimeMeasurement.getRuntime("BACKTEST");
		System.out.println("Replayed " + eventCount + " events in "
				+ difference + "ms. That's "
				+ (eventCount / (double) difference) + " events/ms");

	}

}
