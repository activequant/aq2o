package com.activequant.backtesting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartUtilities;

import com.activequant.backtesting.reporting.BacktestStatistics;
import com.activequant.backtesting.reporting.CSVFileFillExporter;
import com.activequant.backtesting.reporting.HTMLReportGen;
import com.activequant.backtesting.reporting.PNLMonitor;
import com.activequant.domainmodel.AlgoConfig;
import com.activequant.timeseries.CSVExporter;
import com.activequant.timeseries.ChartUtils;
import com.activequant.timeseries.TSContainer2;
import com.activequant.utils.CsvMapWriter;

/**
 * 
 * @author GhostRider
 * 
 */
public abstract class AbstractBacktester {

	protected CSVFileFillExporter fillExporter = new CSVFileFillExporter();
	protected PNLMonitor pnlMonitor;
	protected OrderEventListener oelistener = new OrderEventListener();
	protected BacktestConfiguration btConfig;
	protected AlgoConfig[] algoConfigs;
	protected BacktestStatistics bs;
	protected String reportFolderBase = "reports";
	protected String runId = "";
	protected String targetFolder = reportFolderBase + File.separator + new SimpleDateFormat("yyyyMMdd").format(new Date()) + File.separator+ runId;
	
	
	
	public CSVFileFillExporter getFillExporter() {
		return fillExporter;
	}

	public void setFillExporter(CSVFileFillExporter fillExporter) {
		this.fillExporter = fillExporter;
	}

	public void generateReport() throws IOException {

		
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

		// generate a PNL chart.
		ChartUtilities.saveChartAsPNG(new File(targetFolder + File.separator + "pnl.png"), pnlMonitor.getStaticChart(),
				800, 600);

		try {
			fout = new FileOutputStream(targetFolder + File.separator + "positions.csv");
			CSVExporter c = new CSVExporter(fout, oelistener.getPositionOverTime());
			c.write();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// generate a position chart.
		
		ChartUtilities.saveChartAsPNG(new File(targetFolder + File.separator + "position.png"), ChartUtils.getStepChart("Position", oelistener.getPositionOverTime()),
				800, 600);

		// dump all used algo configs.
		try {
			if (algoConfigs != null) {
				for (AlgoConfig ac : algoConfigs) {
					fout = new FileOutputStream(targetFolder + File.separator + "algoconfig_" + ac.getId() + "_.csv");
					new CsvMapWriter().write(ac.propertyMap(), fout);
					fout.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// dump the backtest config.
		try {
			if (btConfig != null) {

				fout = new FileOutputStream(targetFolder + File.separator + "btconfig_" + btConfig.getId() + "_.csv");
				new CsvMapWriter().write(btConfig.propertyMap(), fout);
				fout.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// calculate some statistics.
		bs = new BacktestStatistics();
		bs.setReportId(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		bs.calcPNLStats(pnlContainer);
		bs.calcPosStats(oelistener.getPositionOverTime());		

		// dump the stats
		try {
			fout = new FileOutputStream(targetFolder + File.separator + "statistics.csv");
			new CsvMapWriter().write(bs.getStatistics(), fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// generate the html report. 
		HTMLReportGen h = new HTMLReportGen(targetFolder);
		h.setTemplateFolder("./src/main/resources/templates");
		h.generate(bs);
		
	}

	public PNLMonitor getPnlMonitor() {
		return pnlMonitor;
	}

	public void setPnlMonitor(PNLMonitor pnlMonitor) {
		this.pnlMonitor = pnlMonitor;
	}

	public BacktestConfiguration getBtConfig() {
		return btConfig;
	}

	public void setBtConfig(BacktestConfiguration btConfig) {
		this.btConfig = btConfig;
	}

	public AlgoConfig[] getAlgoConfigs() {
		return algoConfigs;
	}

	public void setAlgoConfigs(AlgoConfig[] algoConfigs) {
		this.algoConfigs = algoConfigs;
	}

	public String getReportFolderBase() {
		return reportFolderBase;
	}

	public void setReportFolderBase(String reportFolderBase) {
		this.reportFolderBase = reportFolderBase;
	}

	public String getRunId() {
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getTargetFolder() {
		return targetFolder;
	}

	public void setTargetFolder(String targetFolder) {
		this.targetFolder = targetFolder;
	}
	
	public void recalcTargetFolder(){
		targetFolder = reportFolderBase + File.separator + new SimpleDateFormat("yyyyMMdd").format(new Date()) + File.separator+ runId;
	}

}
