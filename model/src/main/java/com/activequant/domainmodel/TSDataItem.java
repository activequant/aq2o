package com.activequant.domainmodel;

public class TSDataItem implements Comparable {

	private Double[] values;
	private TimeStamp ts;

	@Override
	public int compareTo(Object o) {
		if (o instanceof TSDataItem) {
			TSDataItem d2 = (TSDataItem) o;
			return this.ts.compareTo(d2.getTs());
		}
		return 1;
	}

	public TSDataItem( TimeStamp ts, Double[] values) {
		super();
		if (ts == null) {
			throw new IllegalArgumentException("TimeStamp cannot be null");
		}
		this.values = values;
		this.ts = ts;
	}

	// TODO needs implementtaion
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	// TODO needs implementtaion
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public Double[] getValues() {
		return values;
	}

	public void setValues(Double[] values) {
		this.values = values;
	}

	public TimeStamp getTs() {
		return ts;
	}

	public void setTs(TimeStamp ts) {
		this.ts = ts;
	}
}
