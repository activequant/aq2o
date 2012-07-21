package com.activequant.timeseries;

import java.util.ArrayList;
import java.util.List;

import com.activequant.archive.TSContainer;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;

/**
 * time series methods.
 * 
 * @author GhostRider
 * 
 */
public class TSContainerMethods {

	final static long MINUTE_NANO = 60000000000L;

	public void merge(TSContainer ts1, TSContainer ts2) {

	}

	/**
	 * @param in
	 * @param newResolution
	 * @return a NEW container.
	 */
	public TSContainer2 resample(TSContainer2 in, long newResolution) {
		TSContainer2 ret = new TSContainer2(in.getSeriesId(), in.getColumnHeaders(), in.getColumns());
		ret.emptyColumns();
		ret.setResolutionInNanoseconds(newResolution);
		for (TimeStamp ts : in.getTimeStamps()) {
			Object[] row = in.getRow(ts);
			ret.setRow(ts, row);
		}
		return ret;
	}
	
	

	/**
	 * @param in
	 * @param newResolution
	 * @return a NEW container.
	 */
	public TSContainer2 resampleWithSum(TSContainer2 in, long newResolution) {
		TSContainer2 ret = new TSContainer2(in.getSeriesId(), in.getColumnHeaders(), in.getColumns());
		ret.emptyColumns();
		ret.setResolutionInNanoseconds(newResolution);
		long currentSlot = 0L; 
		Double[] val = new Double[in.getNumColumns()];
		for(int i=0;i<val.length;i++)
			val[i] = 0.0; 
		for (TimeStamp ts : in.getTimeStamps()) {
			long slot = ts.getNanoseconds() / newResolution;
			if(slot!=currentSlot){
				currentSlot = slot;
				for(int i=0;i<val.length;i++)
					val[i] = 0.0;  
			}
			Object[] row = in.getRow(ts);
			for(int i=0;i<val.length;i++)
				if(row[i]!=null)
					val[i] += (Double)row[i];
			// 
			ret.setRow(ts, val);
		}
		return ret;
	}

	

	/**
	 * modifys the input ts container
	 * 
	 * @param in
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TSContainer2 overwriteNull(TSContainer2 in) {
		for (int i = 0; i < in.getNumColumns(); i++) {
			Object val = null;
			@SuppressWarnings("rawtypes")
			TypedColumn tc = in.getColumns().get(i);
			for (int j = 0; j < in.getNumRows(); j++) {
				Object cval = tc.get(j);
				if (cval == null)
					cval = val;
				else
					val = cval;
				tc.set(j, val);
			}
		}
		return in;
	}

	@SuppressWarnings("unchecked")
	public TSContainer2 overwriteNull(TSContainer2 in, Object newVal) {
		for (int i = 0; i < in.getNumColumns(); i++) {
			@SuppressWarnings("rawtypes")
			TypedColumn tc = in.getColumns().get(i);
			for (int j = 0; j < in.getNumRows(); j++) {
				Object cval = tc.get(j);
				if (cval == null)
					tc.set(j, newVal);
			}
		}
		return in;
	}

	public TSContainer2 injectTimeStamps(TSContainer2 in, List<TimeStamp> timeStamps) {
		Object[] nullArray = new Object[in.getNumColumns()];
		for (TimeStamp ts : timeStamps) {
			if (!in.getTimeStamps().contains(ts)) {
				in.setRow(ts, nullArray);
			}
		}
		return overwriteNull(in);
	}

	@SuppressWarnings("unchecked")
	public TSContainer2 returns(TSContainer2 in) {
		TSContainer2 ret = new TSContainer2(in.getSeriesId(), in.getColumnHeaders(), in.getColumns());
		ret.emptyColumns();

		for (int j = 0; j < in.getNumColumns(); j++) {
			if (in.getColumns().get(j) instanceof DoubleColumn) {
				ret.getColumns().set(j, ((DoubleColumn) in.getColumns().get(j)).returns());
			}
		}
		return ret;
	}

	public Double[] mean(TSContainer2 in) {
		Double[] ret = new Double[in.getNumColumns()];

		for (int j = 0; j < in.getNumColumns(); j++) {
			if (in.getColumns().get(j) instanceof DoubleColumn) {
				ret[j] = ((DoubleColumn) in.getColumns().get(j)).mean();
			}
		}
		return ret;
	}

	public Double[] std(TSContainer2 in) {
		Double[] ret = new Double[in.getNumColumns()];

		for (int j = 0; j < in.getNumColumns(); j++) {
			if (in.getColumns().get(j) instanceof DoubleColumn) {
				ret[j] = ((DoubleColumn) in.getColumns().get(j)).std();
			}
		}
		return ret;
	}

	public Double[] maxDrawdown(TSContainer2 in) {
		Double[] ret = new Double[in.getNumColumns()];

		for (int j = 0; j < in.getNumColumns(); j++) {
			if (in.getColumns().get(j) instanceof DoubleColumn) {
				ret[j] = ((DoubleColumn) in.getColumns().get(j)).maxDrawdown();
			}
		}
		return ret;
	}

	public Integer[] maxRecoveryTime(TSContainer2 in) {
		Integer[] ret = new Integer[in.getNumColumns()];

		for (int j = 0; j < in.getNumColumns(); j++) {
			if (in.getColumns().get(j) instanceof DoubleColumn) {
				ret[j] = ((DoubleColumn) in.getColumns().get(j)).maxRecoveryTime();
			}
		}
		return ret;
	}

	public List<Double[]> profitPerSlot(final TSContainer2 uncumulated, final TimeFrame tf) throws Exception {

		final List<Double[]> pnlsPerSlot = new ArrayList<Double[]>();

		final int slots = 24 * 60 / tf.getMinutes();

		for (int h = 0; h < uncumulated.getNumColumns(); h++) {

			final Double ret[] = new Double[slots];
			final DoubleColumn dc = (DoubleColumn) uncumulated.getColumns().get(h);

			for (int i = 0; i < uncumulated.getTimeStamps().size(); i++) {

				final TimeStamp ts = uncumulated.getTimeStamps().get(i);
				final int minute = (int) ((ts.getMilliseconds() / 1000 / 60) % (24 * 60));
				final int slot = (int) Math.floor(minute / (double) tf.getMinutes());

				final Double value = dc.get(i);
				if (ret[slot] == null)
					ret[slot] = 0.0;
				ret[slot] += value;

			}
			pnlsPerSlot.add(ret);
		}

		return pnlsPerSlot;
	}

	public List<TimeStamp> getListOfTimeStamps(TimeStamp start, TimeStamp end, TimeFrame resolution) {
		List<TimeStamp> ret = new ArrayList<TimeStamp>();

		TimeStamp currentTimeStamp = start;
		ret.add(currentTimeStamp);
		while (currentTimeStamp.isBefore(end)) {
			currentTimeStamp = new TimeStamp(currentTimeStamp.getNanoseconds() + resolution.getMinutes() * 60l * 1000l
					* 1000l * 1000l);
			ret.add(currentTimeStamp);
		}
		return ret; 
	}

}
