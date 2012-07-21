package com.activequant.timeseries;


/**
 * time series methods.
 * 
 * @author GhostRider
 * 
 */
public class TypedColumnMethods {

	/**
	 * Add to a double column a double value. 
	 * 
	 * @param in the column to operate on
	 * @param value the value to add, f.e. 2.0
	 */
	public void add(DoubleColumn in, double value) {
		for (int i = 0; i < in.size(); i++) {
			Double val = in.get(i);
			if (val != null) {
				val += value;
				in.set(i, val);
			}
		}
	}
}
