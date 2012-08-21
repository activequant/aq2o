package com.brownchipmunk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.trading.AbstractTSBase;
import com.activequant.trading.datamodel.InstrumentTable;
import com.activequant.utils.RenjinCore;

/**
 * Simple Moving Average class that processes a stream event.
 * 
 * @author GhostRider
 * 
 */
public class SimpleDeterministicStrategy extends AbstractTSBase {

	private List<Double> closes = new ArrayList<Double>();
	private RenjinCore R = new RenjinCore();
	private MarketDataInstrument mdi;
	private TradeableInstrument tdi;
	private Logger log = Logger.getLogger(SimpleDeterministicStrategy.class);

	@Override
	public void start() throws Exception {
		System.out.println("Start");
		//
		mdi = new MarketDataInstrument("CSV", "EURUSD");
		tdi = new TradeableInstrument("CSV", "EURUSD");
		// add to local memory environment.
		addInstrument(mdi, tdi);
		getInstrumentTable().setValueAt(0.01,
				getInstrumentTable().getRowIndexOf(mdi.getId()),
				InstrumentTable.Columns.TICKSIZE.colIdx());
		getInstrumentTable().setValueAt(12.50,
				getInstrumentTable().getRowIndexOf(mdi.getId()),
				InstrumentTable.Columns.TICKVALUE.colIdx());
		getInstrumentTable().signalUpdate();
	}

	@Override
	public void stop() throws Exception {
	}

	/**
	 * Called if there is a new market stream data event.
	 * 
	 */
	@Override
	public void process(StreamEvent se) {
		super.process(se);
		TimeStamp ts = se.getTimeStamp();
		Date dt = new Date(ts.getMilliseconds());
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

		if (se.getEventType().equals(ETransportType.MARKET_DATA)) {
			log.info("TimeStamp : "
					+ sdf.format(dt)
					+ ", Price :"
					+ (((MarketDataSnapshot) se).getAskPrices()[0] + ((MarketDataSnapshot) se)
							.getBidPrices()[0]) / 2);
		}
	}
}
