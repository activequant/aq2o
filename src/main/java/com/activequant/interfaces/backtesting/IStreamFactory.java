package com.activequant.interfaces.backtesting;

import com.activequant.domainmodel.backtesting.BacktestConfiguration;
import com.activequant.domainmodel.streaming.StreamEventIterator;
import com.activequant.domainmodel.streaming.TimeStreamEvent;

/**
 * Constructs the stream event iterators from a backtest configuration. 
 * 
 * @author GhostRider
 *
 */
public interface IStreamFactory {
	public StreamEventIterator<? extends TimeStreamEvent>[] getStreamIterators(BacktestConfiguration btConfig) throws Exception;
}
