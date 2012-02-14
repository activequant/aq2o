package com.activequant.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class MapToObject {


    public Object[][] convert(Map<String, Object> mapIn) {

        Object[][] out = new Object[mapIn.size()][2];
        Iterator<String> keyIterator = mapIn.keySet().iterator();
        Iterator<Object> valueIterator = mapIn.values().iterator();
        int row =0; 
        while (keyIterator.hasNext()) {
            //
            String key = keyIterator.next();
            Object value = valueIterator.next();
            out[row][0] = key; 
            out[row][1] = value; 
            row ++; 
        }

        return out; 
    }

}
