package com.activequant.timeseries;

import java.util.List;

public class DoubleColumn extends TypedColumn<Double> {
	private static final long serialVersionUID = 1L;

	public DoubleColumn(List<Double> list) {
		super(list);
	}
	
	public DoubleColumn() {
		super();
	}

	/**
	 * Calculates the cumulated sum
	 * 
	 * @return
	 */
	public DoubleColumn cumsum() {
		DoubleColumn ret = new DoubleColumn();
		Double currentVal = null;
		for (int i = 0; i < super.size(); i++) {
			if (currentVal == null)
				currentVal = super.get(i);
			if (currentVal != null && i > 0 && super.get(i) != null) {
				currentVal = currentVal + super.get(i);
			}
			ret.add(currentVal);
		}
		return ret;
	}

	/**
	 * multiplies each data point by factor. used to scale a data series for
	 * comparison purposes.
	 * 
	 * @return
	 */
	public DoubleColumn multiply(Double value) {
		DoubleColumn ret = new DoubleColumn();
		for (int i = 0; i < super.size(); i++) {
			ret.add(i, super.get(i) == null ? 0 : super.get(i) * value);
		}
		return ret;
	}

	/*
	 * makes this + second doublecolumn
	 */
	public DoubleColumn add(DoubleColumn other) {
		if (super.size() != other.size()) {
			throw new IllegalArgumentException(
					"The columns sizes are different. They must be the same.");
		}
		DoubleColumn ret = new DoubleColumn();
		for (int i = 0; i < super.size(); i++) {
			ret.add((super.get(i) == null ? 0 : super.get(i))
					+ (other.get(i) == null ? 0 : other.get(i)));
		}
		return ret;
	}
	
}
