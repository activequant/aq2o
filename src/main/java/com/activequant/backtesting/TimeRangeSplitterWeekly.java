package com.activequant.backtesting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.backtesting.TimeSetup;
import com.activequant.interfaces.backtesting.ITimeRangeSplitter;

/**
 * Reimplement for YOUR trading algo. Based on the start timestamp, it will
 * generate chunks of seven days length, up to end. The last frame could be
 * shorted than seven days, depending on your end date.
 * 
 * @author GhostRider
 * 
 */
public class TimeRangeSplitterWeekly implements ITimeRangeSplitter {

	public List<TimeSetup> split(TimeStamp start, TimeStamp end) {
		List<TimeSetup> ret = new ArrayList<TimeSetup>();

		while (start.isBefore(end)) {
			TimeStamp localStart = new TimeStamp(start.getNanoseconds());
			//
			Calendar endCal = GregorianCalendar.getInstance();
			endCal.setTime(start.getCalendar().getTime());
			endCal.add(Calendar.DATE, 7);
			TimeStamp localEnd = new TimeStamp(endCal.getTime());
			if (localEnd.isAfter(end))
				localEnd = end;
			TimeSetup setup = new TimeSetup();
			setup.dataReplayStart = localStart;
			setup.tradingStart = localStart;
			setup.tradingEnd = localEnd;
			setup.dataReplayEnd = localEnd;

			ret.add(setup);
			start = localEnd;

		}

		return ret;
	}

}