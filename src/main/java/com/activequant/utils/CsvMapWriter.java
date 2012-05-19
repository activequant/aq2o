package com.activequant.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Writes a map as CSV to disc. 
 * 
 * @author GhostRider
 * 
 */
public class CsvMapWriter {
    private String[] headerNames = new String[]{"KEY", "VALUE"};
    /**
     * DOES NOT CLOSE THE OUTPUT STREAM.
     * 
     * @param map
     * @param out
     * @throws IOException
     */
    public void write(Map<String, Object> map, OutputStream out) throws IOException{
    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
    	Iterator<Entry<String, Object>> it = map.entrySet().iterator();
    	while(it.hasNext()){
    		Entry<String, Object> entry = it.next();
    		String key = entry.getKey();
    		Object value = entry.getValue();
    		bw.write(key);
    		bw.write(value.toString());
    		bw.newLine();
    		bw.flush();
    	}
    	
    	
    }
}
