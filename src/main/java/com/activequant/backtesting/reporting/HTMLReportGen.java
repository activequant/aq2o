package com.activequant.backtesting.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
		templateString = templateString.replaceAll("\\{REPORTID\\}", bt.getReportId());
		
		
		new FileOutputStream(tgtFolder+File.separator +"report.html").write(templateString.getBytes());
		
	}


	public String getTemplateFolder() {
		return templateFolder;
	}


	public void setTemplateFolder(String templateFolder) {
		this.templateFolder = templateFolder;
	}
	
	
}
