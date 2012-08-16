package com.activequant.timeseries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.activequant.domainmodel.TimeStamp;

/** 
 * compatible to AQ CSVExpoter.
 *  
 * @author GhostRider
 *
 */
public class CSVImporter {

	

	
	public TSContainer2 importFile(InputStream inStream) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
		String headerLine = br.readLine(); 
		String columnNames[] = headerLine.split(",");
		List<String> header = new ArrayList<String>();
		List<TypedColumn> cols = new ArrayList<TypedColumn>();
		for(int i=2;i<columnNames.length;i++){
			header.add(columnNames[i]);
			cols.add(new DoubleColumn());
		}
		// ...
		TSContainer2 ret = new TSContainer2("IMPORTED", header, cols);
		
		String l = br.readLine();
		while(l!=null){
			String[] s = l.split(",");
			TimeStamp ts = new TimeStamp(Long.parseLong(s[0]));
			for(int i=2;i<columnNames.length;i++){
				Double val = Double.parseDouble(s[i]);
				ret.setValue(columnNames[i], ts, val);
			}
			
			l = br.readLine(); 
		}
		
		// 
		return ret; 
		
	}
}
