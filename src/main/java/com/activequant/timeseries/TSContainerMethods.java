package com.activequant.timeseries;

import com.activequant.archive.TSContainer;
import com.activequant.domainmodel.TimeStamp;

/**
 * time series methods.
 * 
 * @author GhostRider
 * 
 */
public class TSContainerMethods {
	
	public void merge(TSContainer ts1, TSContainer ts2) {
		
	}
	
	/**
	 * @param in
	 * @param newResolution
	 * @return
	 */
	public TSContainer2 resample(TSContainer2 in, long newResolution){
		TSContainer2 ret = new TSContainer2(in.getSeriesId(), in.getColumnHeaders(), in.getColumns());	
		ret.emptyColumns();
		ret.setResolutionInNanoseconds(newResolution);
		for(TimeStamp ts : in.getTimeStamps()){
			Object[] row = in.getRow(ts);
			ret.setRow(ts, row);
		}		
		return ret; 
	}
}
