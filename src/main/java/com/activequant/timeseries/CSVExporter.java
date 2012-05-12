package com.activequant.timeseries;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;

import com.activequant.domainmodel.TimeStamp;

/**
 * 
 * Convenience class to export a TSContainer to CSV. 
 * 
 * @author GhostRider
 *
 */
public class CSVExporter {

	private OutputStream out; 
	private DecimalFormat dcf = new DecimalFormat("#.########");
	private TSContainer2 data; 
	/**
	 * call write() to trigger writing. 
	 * The write method will NOT close the output stream. 
	 * 
	 * @param out some outputstream, for example a new FileOutputStream(..)
	 * @param data the data to be written to the stream
	 */
	public CSVExporter(OutputStream out, TSContainer2 data){
		this.data = data; 
		this.out = out; 
	}
	
	/**
	 * Does the actual export. 
	 * The write method will NOT close the output stream. 
	 * 
	 * @throws IOException
	 */
	public void write() throws IOException{
		List<TimeStamp> timeStamps = data.getTimeStamps();
		List<String> header = data.getColumnHeaders();
		out.write("TimeStamp,".getBytes());
		for(int i=0;i<header.size();i++){
			out.write(header.get(i).getBytes());
			if(i<(header.size()-1))
				out.write(",".getBytes());
		}
		out.write("\n".getBytes());
		
		for(int i=0;i<timeStamps.size();i++){
			out.write( (""+timeStamps.get(i).getNanoseconds()+",").getBytes() );
			for(int j=0;j<header.size();j++){
				Object o = data.getColumns().get(j).get(i);
				if(o!=null){
					if(o instanceof Double)
						out.write(dcf.format((Double)o).getBytes());
					else
						out.write(o.toString().getBytes());
						
				}				
				if(j<(header.size()-1))
					out.write(",".getBytes());
			}
			out.write("\n".getBytes());
			
		}
		out.flush();
	}
	
}
