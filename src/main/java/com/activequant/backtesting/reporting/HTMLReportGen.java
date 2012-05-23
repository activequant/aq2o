package com.activequant.backtesting.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;

import com.activequant.utils.FileUtils;

public class HTMLReportGen {

	private String templateFolder = "templates";
	private String htmlTemplate = "perfreport.html";
	private String cssFile = "report.css";
	private String tgtFolder; 
	
	
	
	public HTMLReportGen(String targetFolder){
		this.tgtFolder = targetFolder; 
	}
	
	
	public void generate(BacktestStatistics bt) throws FileNotFoundException, IOException{
		// take the template input and generate it. 
		// read-in the entire file. 
		
		FileUtils.copy(templateFolder+File.separator+cssFile, tgtFolder+File.separator + cssFile);
		String templateString = FileUtils.readFully(new FileInputStream(templateFolder+File.separator+htmlTemplate));
		
		// replace the place holders. 		
		templateString = templateString.replace("{REPORTID}", bt.getReportId());
		templateString = templateString.replace("{INSTRUMENTS}", ArrayUtils.toString(bt.getInstrumentIDs()));
		
		int legMarkerStart = templateString.indexOf("<!-- LEG_MARKER_START -->");
		int legMarkerEnd = templateString.indexOf("<!-- LEG_MARKER_END -->");
		
		String instrumentTemplate = templateString.substring(legMarkerStart, legMarkerEnd + "<!-- LEG_MARKER_END -->".length());
		templateString = templateString.replace(instrumentTemplate, "");
		
		int legInsertPoint = templateString.indexOf("<!-- LEG_MARKER_PLACEMENT -->");	
		for(String instrument : bt.getInstrumentIDs()){			
			String t = new String(instrumentTemplate);
			t = t.replace("{INSTRUMENTID}", instrument);
			t = t.replace("{MAXPNL}", ""+bt.getStatistics().get(instrument+".MAXPNL"));
			t = t.replace("{TOTALPLACED}", ""+bt.getStatistics().get(instrument+".TOTALPLACED"));
			t = t.replace("{TOTALFILLS}", ""+bt.getStatistics().get(instrument+".TOTALFILLS"));
			t = t.replace("{TOTALORDERUPDS}", ""+bt.getStatistics().get(instrument+".TOTALORDERUPDS"));
			t = t.replace("{TOTALORDERCNCL}", ""+bt.getStatistics().get(instrument+".TOTALORDERCNCL"));
			t = t.replace("{FINALPNL}", ""+bt.getStatistics().get(instrument+".FINALPNL"));
			t = t.replace("{PNLPERTRADE}", ""+bt.getStatistics().get(instrument+".PNLPERTRADE"));
			
			templateString = templateString.replace("<!-- LEG_MARKER_PLACEMENT -->", "<!-- LEG_MARKER_PLACEMENT -->\n"+t);
		}		
		
		// replacing all null with -
		templateString = templateString.replace("null" , "-");
		
		new FileOutputStream(tgtFolder+File.separator +"report.html").write(templateString.getBytes());		
	}


	public String getTemplateFolder() {
		return templateFolder;
	}


	public void setTemplateFolder(String templateFolder) {
		this.templateFolder = templateFolder;
	}
	
	
}
