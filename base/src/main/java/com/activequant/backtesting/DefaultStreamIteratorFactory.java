package com.activequant.backtesting;

import java.util.ArrayList;
import java.util.List;

import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.backtesting.BacktestConfiguration;
import com.activequant.domainmodel.streaming.StreamEventIterator;
import com.activequant.domainmodel.streaming.TimeStreamEvent;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.backtesting.IStreamFactory;

public class DefaultStreamIteratorFactory implements IStreamFactory {

	private IArchiveFactory archiveFactory;

	public DefaultStreamIteratorFactory(IArchiveFactory archFac) {
		this.archiveFactory = archFac;
	}

	// java nastyness (in my eyes)
	@SuppressWarnings("unchecked")
	@Override
	public StreamEventIterator<? extends TimeStreamEvent>[] getStreamIterators(BacktestConfiguration btConfig) throws Exception {

		List<StreamEventIterator<? extends TimeStreamEvent>> tempList = new ArrayList<StreamEventIterator<? extends TimeStreamEvent>>();
		//
		// for each MDI, instantiate replay stream.
		TimeFrame ohlcResolution = TimeFrame.HOURS_1;
		//
		for (int i = 0; i < btConfig.getMdis().length; i++) {

			ArchiveStreamToMarketDataIterator a1 = new ArchiveStreamToMarketDataIterator(btConfig.getMdis()[i],
					btConfig.getTdis()[i], btConfig.getTimeSetup().dataReplayStart,
					btConfig.getTimeSetup().dataReplayEnd, archiveFactory.getReader(TimeFrame.RAW));
			a1.setQuantityOverride(10000000.0);
			tempList.add(a1);

			//
			ArchiveStreamToOHLCIterator astoi = new ArchiveStreamToOHLCIterator(btConfig.getMdis()[i], ohlcResolution,
					btConfig.getTimeSetup().dataReplayStart, btConfig.getTimeSetup().dataReplayEnd,
					archiveFactory.getReader(ohlcResolution));
			//
			tempList.add(astoi);

		}

		//
		return tempList.toArray(new StreamEventIterator[] {});

	}

}
