package com.activequant.interfaces.backtesting;

import java.util.List;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.backtesting.TimeSetup;

public interface ITimeRangeSplitter {
	public List<TimeSetup> split(TimeStamp start, TimeStamp end);
}
