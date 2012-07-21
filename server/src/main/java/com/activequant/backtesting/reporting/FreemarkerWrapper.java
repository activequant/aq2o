package com.activequant.backtesting.reporting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerWrapper {

	public FreemarkerWrapper(String folderName, String freemarkerTemplate) throws IOException, TemplateException{
		Configuration cfg = new Configuration();
		Template tpl = cfg.getTemplate(freemarkerTemplate);
		OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(folderName+"/report.html"));
		
		// Add the values in the datamodel
		Map datamodel = new HashMap();
		datamodel.put("REPORTID", "123");
		datamodel.put("RESOLUTION", "1m");
		datamodel.put("TIMESTAMPSTART", "20110101");		
		datamodel.put("TIMESTAMPEND", "20120101");
		datamodel.put("MDIS", "--");
		datamodel.put("TDIS", "-");
		datamodel.put("instruments", new String[]{"TOTAL", "PI_EURGBP", "PI_EURUSD"});
		
		
		
		tpl.process(datamodel, output);
	}
	
	/**
	 * @param args
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, TemplateException {
		new FreemarkerWrapper("/home/ustaudinger/work/activequant/trunk/src/test/resources/transactions/", "./src/main/resources/templates/perfreport.tpl");

	}

}
