package com.activequant.backtesting;

import com.activequant.backtesting.reporting.CSVFileFillExporter;

public abstract class AbstractBacktester {
	
	private CSVFileFillExporter fillExporter = new CSVFileFillExporter();
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
		
	}
	
}
