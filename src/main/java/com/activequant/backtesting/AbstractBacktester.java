package com.activequant.backtesting;

import java.io.FileOutputStream;

import com.activequant.backtesting.reporting.CSVFileFillExporter;
import com.activequant.backtesting.reporting.PNLMonitor;
import com.activequant.timeseries.CSVExporter;
import com.activequant.timeseries.TSContainer2;

public abstract class AbstractBacktester {
	
	private CSVFileFillExporter fillExporter = new CSVFileFillExporter();
	private PNLMonitor pnlMonitor;
	protected OrderEventListener oelistener = new OrderEventListener();
	
	public CSVFileFillExporter getFillExporter() {
		return fillExporter;
	}

	public void setFillExporter(CSVFileFillExporter fillExporter) {
		this.fillExporter = fillExporter;
	} 

	public void generateReport(){
		getFillExporter().export(oelistener.getFillEvents());
		// generate PNL report
		TSContainer2 pnlContainer = pnlMonitor.getCumulatedTSContainer();
		FileOutputStream fout;
		try {
			fout = new FileOutputStream("pnl.csv");
			CSVExporter c = new CSVExporter(fout, pnlContainer);
			c.write();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		// calculate some statistics. 
		
		
	}

	public PNLMonitor getPnlMonitor() {
		return pnlMonitor;
	}

	public void setPnlMonitor(PNLMonitor pnlMonitor) {
		this.pnlMonitor = pnlMonitor;
	}
	
}
