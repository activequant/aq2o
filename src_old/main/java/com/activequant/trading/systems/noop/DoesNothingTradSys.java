package com.activequant.trading.systems.noop;

import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.trading.AbstractTSBase;

/**
 * 
 * @author GhostRider
 * 
 */
public class DoesNothingTradSys extends AbstractTSBase {

	private MarketDataInstrument mdi;
	private TradeableInstrument tdi;

	@Override
	public void start() throws Exception {

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
	}
}
