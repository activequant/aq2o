package com.activequant.backtesting;

import com.activequant.backtesting.reporting.FillExporter;

public abstract class AbstractBacktester {
	
	private FillExporter fillExporter;
	private OrderEventListener oelistener = new OrderEventListener();
	
	public FillExporter getFillExporter() {
		return fillExporter;
	}

	public void setFillExporter(FillExporter fillExporter) {
		this.fillExporter = fillExporter;
	} 

	public void generateReport(){
		getFillExporter().export(oelistener.getFillEvents());
	}
	
}
