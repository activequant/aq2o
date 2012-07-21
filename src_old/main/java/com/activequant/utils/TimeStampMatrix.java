package com.activequant.utils;

import java.util.TreeMap;

import com.activequant.domainmodel.TimeStamp;

public class TimeStampMatrix<T> {

    private final int columns;
    private TreeMap<TimeStamp, T[]> matrix = new TreeMap<TimeStamp, T[]>();
    public TimeStampMatrix(int columns){
        this.columns = columns; 
    }
    
    @SuppressWarnings("unchecked")
    public T[] getRow(TimeStamp row){
        if(!matrix.containsKey(row))
            matrix.put(row, (T[]) new Object[columns]);
        return matrix.get(row); 
    }
    
}
