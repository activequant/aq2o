package com.activequant.domainmodel;

public class TimeSeries {

    private TimeStamp[] timeStamps;
    private Double[][] values;
    private String[] columnHeaders;
    
    public TimeStamp[] getTimeStamps() {
        return timeStamps;
    }
    public void setTimeStamps(TimeStamp[] timeStamps) {
        this.timeStamps = timeStamps;
    }
    public Double[][] getValues() {
        return values;
    }
    public void setValues(Double[][] values) {
        this.values = values;
    }
    public String[] getColumnHeaders() {
        return columnHeaders;
    }
    public void setColumnHeaders(String[] columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    
}
