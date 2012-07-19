package com.activequant.timeseries;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author GhostRider
 * 
 * @param <T>
 */
public abstract class TypedColumn<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean dirty = false;
	private boolean lastObjectDirty = false;
	private int lastRowIndex = 0;
	private T lastObject = null;

	public TypedColumn(List list) {
		super(list);
	}

	public TypedColumn() {
		super();
		dirty = true;
		lastObjectDirty = true;
	}

	public TypedColumn(ArrayList column) {
		super(column);
		dirty = true;
		lastObjectDirty = true;
	}

	/**
	 * Overwrite null with a value.
	 * 
	 * @param value
	 */
	public void replaceNull(T value) {
		for (int i = 0; i < super.size(); i++) {
			if (super.get(i) == null)
				super.set(i, value);
		}
		dirty = true;
		lastObjectDirty = true;
	}

	public void add(int targetIndex, T value) {
		super.add(targetIndex, value);
		dirty = true;
		lastObjectDirty = true;
		lastRowIndex = this.size() - 1;
	}

	public boolean add(T value) {
		boolean ret = super.add(value);
		dirty = true;
		lastObjectDirty = true;
		lastRowIndex = this.size() - 1;
		return ret;
	}

	public T get(int row) {

		if (row == (lastRowIndex)) {
			if (lastObjectDirty) {
				lastObject = super.get(lastRowIndex);
				lastObjectDirty = false;
			}
			return lastObject;
		}

		return super.get(row);
	}

	public T set(int row, T value) {
		T t = super.set(row, value);
		dirty = true;
		
		if(row==lastRowIndex){
			lastObject = value; 
			lastObjectDirty = false;
		}
		return t;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setClean() {
		dirty = false;
	}

}
