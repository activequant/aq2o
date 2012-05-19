package com.activequant.backtesting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartUtilities;

import com.activequant.backtesting.reporting.BacktestStatistics;
import com.activequant.backtesting.reporting.CSVFileFillExporter;
import com.activequant.backtesting.reporting.PNLMonitor;
import com.activequant.timeseries.CSVExporter;
import com.activequant.timeseries.TSContainer2;
import com.activequant.utils.CsvMapWriter;

/**
 * 
 * @author GhostRider
 *
 */
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

	public void generateReport() throws IOException{
		
		String targetFolder = "reports"+File.separator+new SimpleDateFormat("yyyyMMdd").format(new Date());
		new File(targetFolder).mkdirs();
		getFillExporter().export(targetFolder, oelistener.getFillEvents());
		// generate PNL report
		TSContainer2 pnlContainer = pnlMonitor.getCumulatedTSContainer();
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(targetFolder + File.separator + "pnl.csv");
			CSVExporter c = new CSVExporter(fout, pnlContainer);
			c.write();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// generate a chart. 
		ChartUtilities.saveChartAsPNG(new File(targetFolder+File.separator+"pnl.png"), pnlMonitor.getStaticChart(), 800,600);
	
		// calculate some statistics. 
		BacktestStatistics bs = new BacktestStatistics();
		bs.calculateStatistics(pnlContainer);
		
		// dump the stats
		try {
			fout = new FileOutputStream(targetFolder + File.separator + "statistics.csv");
			new CsvMapWriter().write(bs.getStatistics(), fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public PNLMonitor getPnlMonitor() {
		return pnlMonitor;
	}

	public void setPnlMonitor(PNLMonitor pnlMonitor) {
		this.pnlMonitor = pnlMonitor;
	}
	
}
