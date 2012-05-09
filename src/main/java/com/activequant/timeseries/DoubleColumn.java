package com.activequant.timeseries;

public class DoubleColumn extends TypedColumn<Double> {
	private static final long serialVersionUID = 1L;

	/**
	 * Calculates the cumulated sum 
	 * @return
	 */
	public DoubleColumn cumsum(){
		DoubleColumn ret = new DoubleColumn();
		Double currentVal = null; 
		for(int i=0;i<super.size();i++){
			if(currentVal==null)
				currentVal = super.get(i);
			if(currentVal!=null && i>0 && super.get(i)!=null){
				currentVal = currentVal + super.get(i);
			}
			ret.add(currentVal);
		}
		return ret; 
	}
	
}
