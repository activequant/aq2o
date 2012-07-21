package com.activequant.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
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
	private String[] headerNames = new String[] { "KEY", "VALUE" };
	private DecimalFormat dcf = new DecimalFormat("####################.##");
	
	/**
	 * DOES NOT CLOSE THE OUTPUT STREAM.
	 * 
	 * @param map
	 * @param out
	 * @throws IOException
	 */
	public void write(Map<String, Object> map, OutputStream out) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value != null && key!=null) {
				bw.write(key);
				bw.write(",");
				if(value.getClass().isAssignableFrom(Double.class) || value.getClass().isAssignableFrom(Long.class)){
					bw.write(dcf.format(value));
				}
				else
					bw.write(value.toString());
				bw.newLine();
				bw.flush();
			}
		}

	}
}
