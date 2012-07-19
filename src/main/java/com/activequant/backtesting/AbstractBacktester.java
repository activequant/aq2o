package com.activequant.backtesting;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.activequant.backtesting.reporting.BacktestStatistics;
import com.activequant.backtesting.reporting.HTMLReportGen;
import com.activequant.backtesting.reporting.PNLMonitor;
import com.activequant.domainmodel.AlgoConfig;
import com.activequant.domainmodel.backtesting.BacktestConfiguration;
import com.activequant.domainmodel.backtesting.SimulationReport;
import com.activequant.timeseries.TSContainer2;
import com.activequant.timeseries.TSContainerMethods;

/**
 * 
 * @author GhostRider
 * 
 */
public abstract class AbstractBacktester {

	protected PNLMonitor pnlMonitor;
	protected OrderEventListener oelistener = new OrderEventListener();
	protected BacktestConfiguration btConfig;
	protected AlgoConfig[] algoConfigs;
	protected BacktestStatistics bs;
	protected String reportFolderBase = "reports";
	protected String runId = "";
	protected String targetFolder = reportFolderBase + File.separator + new SimpleDateFormat("yyyyMMdd").format(new Date()) + File.separator+ runId;
	protected String templateFolder = "./src/main/resources/templates";
	protected Logger log = Logger.getLogger(AbstractBacktester.class);
	
	public AbstractBacktester(BacktestConfiguration btConfig){
		this.btConfig = btConfig;
	}
	
	public void generateReport(String localTargetFolder, String localTemplateFolder) throws IOException {
		HTMLReportGen h = new HTMLReportGen(localTargetFolder, localTemplateFolder);
		if(oelistener.getFillEvents().size()>0){
			h.genReport(algoConfigs, oelistener, pnlMonitor, btConfig);
		}
		else{
			log.warn("No trades were generated!");
			System.out.println("No trades were generated.");
		}
		
	}
	

	public SimulationReport generateReport() throws IOException {
		SimulationReport sr = new SimulationReport();
		
		TSContainerMethods tcm = new TSContainerMethods();
		TSContainer2 pnlContainer = pnlMonitor.getCumulatedTSContainer();		
		tcm.overwriteNull(pnlContainer);
		tcm.overwriteNull(pnlContainer, 0.0);
		sr.setPnlSeries(pnlContainer);
		
		// 
		
		HTMLReportGen h = new HTMLReportGen(targetFolder, templateFolder);
		if(oelistener.getFillEvents().size()>0){
			h.genReport(algoConfigs, oelistener, pnlMonitor, btConfig);
		}
		else{
			log.warn("No trades were generated!");
			System.out.println("No trades were generated.");
		}
		
		// fill the sr
		
		return sr; 
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

	public String getTemplateFolder() {
		return templateFolder;
	}

	public void setTemplateFolder(String templateFolder) {
		this.templateFolder = templateFolder;
	}

}
