package com.activequant.backtesting.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.activequant.dao.DaoException;
import com.activequant.dao.IReportDao;
import com.activequant.domainmodel.Report;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ExtTrsctFileReporting {
	
	private IReportDao reportDao; 
	
	public ExtTrsctFileReporting(IReportDao reportDao){
		this.reportDao = reportDao; 
	}
	
	
	public void run(String reportId, String folder) throws DaoException, FileNotFoundException, IOException{
		Report r = reportDao.load(reportId);
		r.setStatus("PROCESSING");
		reportDao.update(r);
		
		try{
		
			new TransactionInputToReport(
					folder + "/transactions.csv", null,
					folder,
					"localhost");
						
			// 
			r.setStatus("DONE");
			reportDao.update(r);	
		}
		catch(Exception ex){
			r.setStatus("ERROR: " + ex);
			reportDao.update(r);
		}
	}
	
	/**
	 * @param args
	 */
//	public static void main(String[] args) throws Exception {
//		new ExtTrsctFileReporting(null).run(args[0], args[1]);
//	}
	
	public static void main(String[] args)
	{
		// Add the values in the datamodel
		Map datamodel = new HashMap();
		datamodel.put("pet", "Bunny");
		datamodel.put("number", new Integer(6));

		// Process the template using FreeMarker
		try {
			freemarkerDo(datamodel, "./src/main/resources/templates/perfreport.tpl");
		}
		catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	// Process a template using FreeMarker and print the results
	static void freemarkerDo(Map datamodel, String template) throws Exception
	{
		Configuration cfg = new Configuration();
		Template tpl = cfg.getTemplate(template);
		OutputStreamWriter output = new OutputStreamWriter(System.out);

		tpl.process(datamodel, output);
	}

}
