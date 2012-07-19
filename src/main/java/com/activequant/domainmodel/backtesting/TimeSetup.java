package com.activequant.domainmodel.backtesting;

import com.activequant.domainmodel.TimeStamp;

public class TimeSetup {
	public TimeStamp dataReplayStart, dataReplayEnd, tradingStart, tradingEnd;

	public String toString() {
		return dataReplayStart.getCalendar().getTime() + " / " + tradingStart.getCalendar().getTime() + " / "
				+ tradingEnd.getCalendar().getTime() + " / " + dataReplayEnd.getCalendar().getTime();
	}
}