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
	
	public double[] asDouble(){
		double[] ret = new double[size()];
		for(int i=0;i<ret.length;i++){
			if(get(i)!=null)
				ret[i] = get(i);
		}
		return ret; 
		
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
			Double val = super.get(i);
			if(currentVal!=null && val!=null){
				currentVal += val; 
			}
			else
				currentVal = val; 
			
			ret.add(currentVal);
		}
		return ret;
	}

	
	/**
	 * Calculates the sum
	 * 
	 * @return
	 */
	public Double sum() {
		Double currentVal = null;
		for (int i = 0; i < super.size(); i++) {
			if (currentVal == null)
				currentVal = super.get(i);
			if (currentVal != null && i > 0 && super.get(i) != null) {
				currentVal = currentVal + super.get(i);
			}
		}
		return currentVal;
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

	public DoubleColumn returns() {
		DoubleColumn ret = new DoubleColumn();
		ret.add(0, null);
		for (int i = 1; i < this.size(); i++) {
			Double currentVal = this.get(i);
			Double formerVal = this.get(i - 1);
			if(formerVal!=null){
				ret.add(i, (currentVal - formerVal)/formerVal);
			}
		}
		return ret;
	}

	// mean of returns
	public Double mean() {
		Double sum = 0.0;
		for (int i = 1; i < this.size(); i++) {
			sum = sum + (this.get(i) - this.get(i - 1)) / this.get(i - 1);
		}
		return sum / (this.size() - 1);
	}

	// standard deviation of returns
	public Double std() {
		Double sum1 = 0.0;
		Double sum2 = 0.0;
		for (int i = 1; i < this.size(); i++) {
			Double ret = (this.get(i) - this.get(i - 1)) / this.get(i - 1);
			sum1 = sum1 + ret;
			sum2 = sum2 + ret * ret;
		}
		Double var = sum2/(this.size() - 1) - (sum1/(this.size()-1))*(sum1/(this.size()-1));
		Double std = Math.sqrt(var);
		return Math.sqrt(var); 
	}
	
	// maximum drawdown
	// implementation is taken from http://en.wikipedia.org/wiki/Drawdown_%28economics%29
	// except multiplier 100 on dd calculation line - MATLAB does not use this multiplier
	// test is taken from matlab
	public Double maxDrawdown() {
		Double mdd = 0.0;
		Double peak = Double.MIN_VALUE;
		Double dd =0.0;
		
		for(int i=0; i<this.size(); i++) {
			if(this.get(i) > peak) {
				peak = this.get(i);
			}
			else {
				dd = (peak - this.get(i)) / peak;
				if(dd > mdd) {
					mdd = dd;
				}
			}
		}
		return mdd;
	}
	
	// can not have from/to timestamp parameters, 
	// because there is no timestamps column 
	public Integer maxRecoveryTime() {
		Integer maxPeriod = 0;
		Integer currentLength = 0;
		Double currentVal = Double.MIN_VALUE;
		
		for(int i=0; i<this.size(); i++) {
			if(this.get(i) < currentVal) {
				currentLength = currentLength + 1;
			}
			if(this.get(i) > currentVal) {
				currentLength = 0;
				currentVal = this.get(i);
			}
			if(currentLength>maxPeriod) {
				maxPeriod = currentLength;
			}
		}
		return maxPeriod;
	}
}
