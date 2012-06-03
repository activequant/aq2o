package com.activequant.backtesting.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;

import com.activequant.backtesting.BacktestConfiguration;
import com.activequant.backtesting.OrderEventListener;
import com.activequant.domainmodel.AlgoConfig;
import com.activequant.exceptions.InvalidDate8Time6Input;
import com.activequant.timeseries.CSVExporter;
import com.activequant.timeseries.ChartUtils;
import com.activequant.timeseries.TSContainer2;
import com.activequant.timeseries.TSContainerMethods;
import com.activequant.utils.CsvMapWriter;
import com.activequant.utils.Date8Time6Parser;
import com.activequant.utils.FileUtils;

public class HTMLReportGen {
	protected CSVFileFillExporter fillExporter = new CSVFileFillExporter();

	private Logger log = Logger.getLogger(HTMLReportGen.class);
	private String templateFolder = "templates";
	private String htmlTemplate = "perfreport.html";
	private String cssFile = "report.css";
	private String targetFolder;

	public HTMLReportGen(String targetFolder, String templateFolder) {
		this.targetFolder = targetFolder;
		this.templateFolder = templateFolder;
	}

	public void genReport(AlgoConfig[] algoConfigs, OrderEventListener oelistener, PNLMonitor pnlMonitor,
			BacktestConfiguration btConfig) throws IOException {
		TSContainerMethods tcm = new TSContainerMethods();
		new File(targetFolder).mkdirs();
		fillExporter.export(targetFolder, oelistener.getFillEvents());
		// generate PNL report
		TSContainer2 pnlContainer = pnlMonitor.getCumulatedTSContainer();
		tcm.overwriteNull(pnlContainer);
		tcm.overwriteNull(pnlContainer, 0.0);

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

		TSContainer2 posSeries = oelistener.getPositionOverTime();
		tcm.overwriteNull(posSeries, 0.0);
		try {
			fout = new FileOutputStream(targetFolder + File.separator + "positions.csv");
			CSVExporter c = new CSVExporter(fout, posSeries);
			c.write();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// generate a position chart, make sure we align it to the pnlContainer

		tcm.injectTimeStamps(posSeries, pnlContainer.getTimeStamps());

		ChartUtilities.saveChartAsPNG(new File(targetFolder + File.separator + "position.png"),
				ChartUtils.getStepChart("Position", posSeries), 800, 600);

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
		BacktestStatistics bs = new BacktestStatistics();
		bs.setReportId(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		bs.calcPNLStats(pnlContainer);
		bs.calcPosStats(oelistener.getPositionOverTime());
		bs.populateOrderStats(oelistener);

		// dump the stats
		try {
			fout = new FileOutputStream(targetFolder + File.separator + "statistics.csv");
			new CsvMapWriter().write(bs.getStatistics(), fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		generate(algoConfigs, btConfig, bs);

	}

	public void generate(AlgoConfig[] algoConfigs, BacktestConfiguration bc, BacktestStatistics bt)
			throws FileNotFoundException, IOException {
		// take the template input and generate it.
		// read-in the entire file.
		log.info("Generating report.");
		log.info("Reading template from " + templateFolder + File.separator + htmlTemplate);

		FileUtils.copy(templateFolder + File.separator + cssFile, targetFolder + File.separator + cssFile);
		String templateString = FileUtils
				.readFully(new FileInputStream(templateFolder + File.separator + htmlTemplate));

		log.info("Populating generic section.");
		// replace the place holders.
		templateString = templateString.replace("{REPORTID}", bt.getReportId());
		if (bc != null) {
			templateString = templateString.replace("{MDIS}", ArrayUtils.toString(bc.getMdis()));
			templateString = templateString.replace("{TDIS}", ArrayUtils.toString(bc.getTdis()));
			try {
				templateString = templateString.replace("{BACKTESTSTART}",
						"" + new Date8Time6Parser().fromDouble(bc.getDate8Time6Start()).getCalendar().getTime());
				templateString = templateString.replace("{BACKTESTEND}",
						"" + new Date8Time6Parser().fromDouble(bc.getDate8Time6End()).getCalendar().getTime());
			} catch (InvalidDate8Time6Input e) {
				e.printStackTrace();
			}
			templateString = templateString.replace("{REPORTRESOLUTION}", bc.getResolutionTimeFrame());
		}

		int acMarkerStart = templateString.indexOf("<!-- AC_MARKER_START -->");
		int acMarkerEnd = templateString.indexOf("<!-- AC_MARKER_END -->");
		String acTemplate = templateString.substring(acMarkerStart + "<!-- AC_MARKER_START -->".length(), acMarkerEnd);
		if (algoConfigs != null) {
			StringBuffer acSection = new StringBuffer();
			for (AlgoConfig ac : algoConfigs) {
				String acTemplateLocal = new String(acTemplate);
				log.info("Replacing algo config section. ");

				int acEntryMarkerStart = acTemplateLocal.indexOf("<!-- AC_ENTRY_START -->");
				int acEntryMarkerEnd = acTemplateLocal.indexOf("<!-- AC_ENTRY_END -->");

				String acEntryTemplate = acTemplateLocal.substring(
						acEntryMarkerStart + "<!-- AC_ENTRY_START -->".length(), acEntryMarkerEnd);

				StringBuffer entryRep = new StringBuffer();
				// build the entry rows.
				Iterator<Entry<String, Object>> entryIter = ac.propertyMap().entrySet().iterator();
				while (entryIter.hasNext()) {
					Entry<String, Object> e = entryIter.next();
					String entry = acEntryTemplate.replace("{KEY}", e.getKey());
					if(e.getValue()!=null)
						entry = entry.replace("{VALUE}", e.getValue().toString());
					else
						entry = entry.replace("{VALUE}", "-");
					entryRep.append(entry).append("\n");
				}
				acTemplateLocal = acTemplateLocal.substring(0, acEntryMarkerStart) + entryRep.toString()+ acTemplateLocal.substring(acEntryMarkerEnd);
				
				//
				acSection.append(acTemplateLocal).append("\n");

			}
			// replace the section in the report. 
			templateString = templateString.substring(0, acMarkerStart) + acSection.toString()  + templateString.substring(acMarkerEnd);
			
		} else {
			templateString = templateString.substring(0, acMarkerStart) + templateString.substring(acMarkerEnd);
		}

		log.info("Replacing leg section");
		int legMarkerStart = templateString.indexOf("<!-- LEG_MARKER_START -->");
		int legMarkerEnd = templateString.indexOf("<!-- LEG_MARKER_END -->");

		String instrumentTemplate = templateString.substring(legMarkerStart,
				legMarkerEnd + "<!-- LEG_MARKER_END -->".length());
		templateString = templateString.replace(instrumentTemplate, "");

		int legInsertPoint = templateString.indexOf("<!-- LEG_MARKER_PLACEMENT -->");
		for (String instrument : bt.getInstrumentIDs()) {
			log.info("Replacing for " + instrument);
			String t = new String(instrumentTemplate);
			t = t.replace("{INSTRUMENTID}", instrument);
			t = t.replace("{MAXPNL}", "" + bt.getStatistics().get(instrument + ".MAXPNL"));
			t = t.replace("{TOTALPLACED}", "" + bt.getStatistics().get(instrument + ".TOTALPLACED"));
			t = t.replace("{TOTALFILLS}", "" + bt.getStatistics().get(instrument + ".TOTALFILLS"));
			t = t.replace("{TOTALORDERUPDS}", "" + bt.getStatistics().get(instrument + ".TOTALORDERUPDS"));
			t = t.replace("{TOTALORDERCNCL}", "" + bt.getStatistics().get(instrument + ".TOTALORDERCNCL"));
			t = t.replace("{FINALPNL}", "" + bt.getStatistics().get(instrument + ".FINALPNL"));
			t = t.replace("{PNLPERTRADE}", "" + bt.getStatistics().get(instrument + ".PNLPERTRADE"));

			templateString = templateString.replace("<!-- LEG_MARKER_PLACEMENT -->", "<!-- LEG_MARKER_PLACEMENT -->\n"
					+ t);
		}

		// replacing all null with -
		templateString = templateString.replace("null", "-");
		log.info("All replaced.");
		new FileOutputStream(targetFolder + File.separator + "report.html").write(templateString.getBytes());
		log.info("Written.");
	}

	public String getTemplateFolder() {
		return templateFolder;
	}

	public void setTemplateFolder(String templateFolder) {
		this.templateFolder = templateFolder;
	}

}
