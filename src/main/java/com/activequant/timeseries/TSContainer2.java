package com.activequant.timeseries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.activequant.domainmodel.TimeStamp;

/**
 * Trivial container class, even without getters and setters.
 * 
 * @author ustaudinger
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TSContainer2 {

	// New implementation started here
	private List<TypedColumn> columns = new ArrayList<TypedColumn>();
	private List<TimeStamp> timeStamps = new ArrayList<TimeStamp>();
	private List<String> columnHeaders = new ArrayList<String>();

	
	// to specify a series granularity, 0 for no time-framing.
	private long resolutionInNanoseconds = 0;

	
	/**
	 * contains the series ID.
	 */
	private final String seriesId;

	private int windowSize = 0; 

	public TSContainer2(final String seriesId, List<String> columnHeaders, List<TypedColumn> columns) {
		// initialize the data array
		this.columns = new ArrayList<TypedColumn>();
		// initialize the column headers. Copying into an array list to get rid of possible immutable classes. 
		this.columnHeaders = new ArrayList<String>(columnHeaders);
		// initialize the data lists.
		this.columns = new ArrayList<TypedColumn>(columns);
		this.seriesId = seriesId;
	}

	// It is not possible to change SeriesId after creation of the time series
	public String getSeriesId() {
		return seriesId;
	}

	public int getNumRows() {
		if (this.columns.size() == 0)
			return 0;
		return this.columns.get(0).size();
	}

	// returns number of lists including timestamps list
	public int getNumColumns() {
		return this.columns.size();
	}

	public TypedColumn getColumn(String headerName) {
		if (headerName == null) {
			throw new IllegalArgumentException("Header name is null");
		}
		//
		int index = getColumnIndex(headerName);
		//
		if (index > -1)
			return columns.get(index);

		return null;
	}

	public int getColumnIndex(String headerName) {
		for (int i = 0; i < this.columnHeaders.size(); i++) {
			if (this.columnHeaders.get(i).equals(headerName))
				return i;
		}
		// not found.
		return -1;
	}

	public void addColumn(String headerName, TypedColumn tc) {
		if (headerName == null) {
			throw new IllegalArgumentException("Header name is null");
		}

		// 1. add header.
		this.columnHeaders.add(headerName);
		// 2. add column
		this.columns.add(tc);
		if(tc.size()!=getNumRows()){
			tc.clear();
		}
		// initialize column with null values.
		for (int i = 0; i < getNumRows(); i++) {
			tc.add(null);
		}
	}

	public void deleteColumn(String headerName) {
		int index = getColumnIndex(headerName);
		if (index > -1){
			this.columns.remove(index);
			this.columnHeaders.remove(index);
		}
	}

	/**
	 * Set a value at a timestamp. if the timestamp does not exist, it will be
	 * inserted. Otherwise, it will overwrite the existing values.
	 * 
	 * @param ts
	 * @param values
	 */
	public void setRow(TimeStamp ts, Object... values) {
		if (ts == null) {
			throw new IllegalArgumentException("Timestamp is null");
		}
		if (values.length != getNumColumns()) {
			throw new IllegalArgumentException("Values has length " + values.length + " but expected "
					+ this.columns.size());
		}
		int targetIndex = Collections.binarySearch(timeStamps, ts);
		
		boolean overwrite = false;
		if (targetIndex >= 0 && timeStamps.get(targetIndex).equals(ts))
			overwrite = true;
		
		if (overwrite) {			
			for (int i = 0; i < values.length; i++) {
				columns.get(i).set(targetIndex, values[i]);
			}			
		}
		else{
			targetIndex = Math.abs(targetIndex + 1);
			// checking if the list is at the maximum window size. 
			if(windowSize == timeStamps.size() && timeStamps.size() > 0) {
				if(targetIndex == 0) {
					delete(getNumRows() - 1);
				}
				else{
					delete(0);
					targetIndex--;
				}
			}
			timeStamps.add(targetIndex, ts);
			for (int i = 0; i < values.length; i++) {
				columns.get(i).add(targetIndex, values[i]);
			}
		}
	}
	
	/**
	 * Set a value in a column for a specific timestamp. Will insert column if not found. Will insert timestamp if not found. 
	 * Rows at newly inserted Timestamp will be initialized with null. 
	 * 
	 * @param headerName
	 * @param ts
	 * @param value
	 */
	
	public void setValue(String headerName, TimeStamp ts, Double value){
		int colIdx = getColumnIndex(headerName);
		if(colIdx == -1){
			// add a column. 
			addColumn(headerName, new DoubleColumn());
		}
		// check if we find that timestamp. 
		int rowIdx = getIndex(ts);
		if(rowIdx < 0 ){
			//add a row.
			setRow(ts, new Object[getNumColumns()]);
		}
		rowIdx = getIndex(ts);
		getColumn(headerName).set(rowIdx, value);
		
	}

	public void delete(TimeStamp ts) {
		if (ts == null) {
			throw new IllegalArgumentException("Timestamp is null");
		}
		int index = Collections.binarySearch(this.timeStamps, ts);
		if(index<0) return;
		if(this.timeStamps.get(index).equals(ts)){
			delete(index);
		}
	}

	public void delete(int rowIndex) {
		timeStamps.remove(rowIndex);
		for (int i = 0; i < columns.size(); i++) {
			columns.get(i).remove(rowIndex);
		}
	}

	public void delete(TimeStamp from, TimeStamp to) {
		int indexFrom = getIndex(from);
		int count = getIndex(to) - indexFrom + 1;

		for (int i = 0; i < count; i++) {
			delete(indexFrom);
		}
	}

	public Object[] getRow(TimeStamp ts) {
		int index = getIndex(ts);

		Object[] data = new Object[getNumColumns()];		
		for (int i = 0; i < getNumColumns()-1; i++) {
			data[i] = columns.get(i).get(index);
		}

		return data;
	}

	public TimeStamp getTime(int index) {
		return (TimeStamp) this.timeStamps.get(index);
	}

	public int getIndex(TimeStamp ts) {
		if (ts == null) {
			throw new IllegalArgumentException("Timestamp is null");
		}
		int index = Collections.binarySearch(timeStamps, ts);
		if (index < 0) {
			return -1;
		}
		return index;
	}

	// if there is a match - return index, else - return index of the 
	// closest timestamp before given one
	public int getIndexBeforeOrEqual(TimeStamp ts) {
		int targetIndex = Collections.binarySearch(timeStamps, ts);
		if(targetIndex < 0) {
			return Math.abs(targetIndex + 2);
		}
		return targetIndex;
	}
	
	// if there is a match, then index + 1 is returned
	// if there is not match, then closest timestamp before + 1 is returned
	public int getIndexAfter(TimeStamp ts) {
		int targetIndex = Collections.binarySearch(timeStamps, ts);
		if(targetIndex < 0) {
			return Math.abs(targetIndex + 1);
		}
		return targetIndex + 1;
	}
	
	// not sure if this needed
	// could be implemented as Collections.max(timeSeries.getColumn("Ask"));
	// the Object should implement Comparable interface
	// or class can maintain two lists of max/min values
	public Object getMax(String headerName) {
		List l = getColumn(headerName);
		Object o = l.get(0);
		if(o instanceof Comparable) {
			return Collections.max(l);
		}
		return null;
	}
	
	// not sure if this needed
	// could be implemented as Collections.min(timeSeries.getColumn("Ask"));
	// the Object should implement Comparable interface
	// or class can maintain two lists of max/min values
	public Object getMin(String headerName) {
		List l = getColumn(headerName);
		Object o = l.get(0);
		if(o instanceof Comparable) {
			return Collections.min(l);
		}
		return null;
	}

	public TimeStamp getMinTimestamp() {
		return timeStamps.get(0);
	}
	
	public TimeStamp getMaxTimestamp() {
		return timeStamps.get(timeStamps.size() - 1);
	}

	public void setMaxWindow(int windowSize) {
		this.windowSize = windowSize;
	}
	
	public int getMaxWindow() {
		return windowSize;
	}
	
	
	public List<TypedColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TypedColumn> columns) {
		this.columns = columns;
	}

	public List<TimeStamp> getTimeStamps() {
		return timeStamps;
	}

	public void setTimeStamps(List<TimeStamp> timeStamps) {
		this.timeStamps = timeStamps;
	}

	// delete non-inclusive
	public void deleteBefore(TimeStamp ts) {
		deleteBefore(getIndex(ts));
	}
	
	// delete inclusive
	public void deleteBefore(int index) {
		if (index < 0  || index > this.timeStamps.size()) {
			throw new IllegalArgumentException("index is out of range");
		}
		for (int i = 0; i <= index; i++) {
			delete(0);
		}
	}

	// delete inclusive
	public void deleteAfter(int index) {
		if (index < 0  || index > this.timeStamps.size()) {
			throw new IllegalArgumentException("index is out of range");
		}
		int count = this.timeStamps.size() - index;
		for (int i = 0; i<count; i++) {
			delete(index);
		}
	}
	
	public List<String> getColumnHeaders() {
		return columnHeaders;
	}

	public void setColumnHeaders(List<String> columnHeaders) {
		this.columnHeaders = columnHeaders;
	}

}