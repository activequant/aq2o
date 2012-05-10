package com.activequant.timeseries;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author GhostRider
 *
 * @param <T>
 */
public abstract class TypedColumn<T> extends ArrayList<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TypedColumn(List list) {
		super(list);
	}

	public TypedColumn() {
		super();
	}

	public TypedColumn(ArrayList column) {
		super(column);
	}

	/**
	 * Overwrite null with a value. 
	 * @param value
	 */
	public void replaceNull(T value){
		for(int i=0;i<super.size();i++){
			if(super.get(i)==null)super.set(i, value);
		}
	}

}
