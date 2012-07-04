package com.activequant.backtesting.reporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartUtilities;

import com.activequant.backtesting.OrderEventListener;
import com.activequant.domainmodel.AlgoConfig;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.timeseries.CSVExporter;
import com.activequant.timeseries.ChartUtils;
import com.activequant.timeseries.TSContainer2;
import com.activequant.trading.PositionRiskCalculator;
import com.activequant.transport.ITransportFactory;
import com.activequant.transport.memory.InMemoryTransportFactory;
import com.activequant.utils.CsvMapWriter;

public class TransactionInputToReport {

	private String fileName; 
	
	public TransactionInputToReport(String fileName) throws Exception{
		this.fileName = fileName; 
		CSVFileFillExporter fillExporter = new CSVFileFillExporter();

		File f = new File(fileName); 
		ITransportFactory transFac = new InMemoryTransportFactory(); 
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		// pos risk calc listens to executions and price events. 
		PositionRiskCalculator prc = new PositionRiskCalculator(null);
		prc.setTransportFactory(transFac);
		// pnl monitor listens to risk events .. these come from the position risk calculator
		PNLMonitor pnlMonitor = new PNLMonitor(transFac);
		
		OrderEventListener oel = new OrderEventListener();
		String l = br.readLine();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		
		while(l!=null){
			String[] p = l.split(",");
			String inst = p[0];
			TimeStamp ts = new TimeStamp(sdf.parse(p[1]));
			String dir = p[2];
			Double price = Double.parseDouble(p[4]);
			Double quantity = Double.parseDouble(p[3]);
			prc.execution(ts, inst, price, (dir.startsWith("B")?1:-1) * quantity);
			OrderFillEvent ofe = new OrderFillEvent();
			ofe.setOptionalInstId(inst);
			ofe.setFillPrice(price);
			ofe.setFillAmount(quantity);
			ofe.setSide(dir);
			ofe.setRefOrderId("-");
			ofe.setCreationTimeStamp(ts);
			oel.eventFired(ofe);
			
			l = br.readLine();
		}
		
		
		TSContainer2 tsc = pnlMonitor.getCumulatedTSContainer();
		
		String targetFolder = "./reports2";
		//////////////// 
		new File(targetFolder).mkdirs();
		fillExporter.export(targetFolder, oel.getFillEvents());
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
			CSVExporter c = new CSVExporter(fout, oel.getPositionOverTime());
			c.write();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// generate a position chart.
		
		ChartUtilities.saveChartAsPNG(new File(targetFolder + File.separator + "position.png"), ChartUtils.getStepChart("Position", oel.getPositionOverTime()),
				800, 600);


		// calculate some statistics.
		BacktestStatistics bs = new BacktestStatistics();
		bs.setReportId(new SimpleDateFormat("yyyyMMdd").format(new Date()));
		bs.calcPNLStats(pnlContainer);
		bs.calcPosStats(oel.getPositionOverTime());		
		bs.populateOrderStats(oel);
		
		// dump the stats
		try {
			fout = new FileOutputStream(targetFolder + File.separator + "statistics.csv");
			new CsvMapWriter().write(bs.getStatistics(), fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// generate the html report. 
		HTMLReportGen h = new HTMLReportGen(targetFolder, "./src/main/resources/templates");
		h.genReport(new AlgoConfig[]{}, oel, pnlMonitor, null);
		
	}
	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {
		new TransactionInputToReport("/home/ustaudinger/MOSTRA_ALL_Trades.csv");

	}

}
