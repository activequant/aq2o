package com.activequant.backtesting.reporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartUtilities;

import com.activequant.backtesting.OrderEventListener;
import com.activequant.domainmodel.AlgoConfig;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.timeseries.CSVExporter;
import com.activequant.timeseries.ChartUtils;
import com.activequant.timeseries.DoubleColumn;
import com.activequant.timeseries.TSContainer2;
import com.activequant.timeseries.TypedColumn;
import com.activequant.trading.PositionRiskCalculator;
import com.activequant.transport.ITransportFactory;
import com.activequant.transport.memory.InMemoryTransportFactory;
import com.activequant.utils.CsvMapWriter;

public class TransactionInputToReport {

	private String fileName;
	private String targetFolder = "./reports2";
	private String reportCurrency;

	public TransactionInputToReport(String transactionsFile, String tgt) throws Exception {
		if (tgt != null)
			targetFolder = tgt;
		this.fileName = transactionsFile;
		CSVFileFillExporter fillExporter = new CSVFileFillExporter();
		//
		File f = new File(transactionsFile);
		ITransportFactory transFac = new InMemoryTransportFactory();
		//
		BufferedReader br = new BufferedReader(new FileReader(f));
		// pos risk calc listens to executions and price events.
		PositionRiskCalculator prc = new PositionRiskCalculator(null);
		prc.setTransportFactory(transFac);
		// pnl monitor listens to risk events .. these come from the position
		// risk calculator
		PNLMonitor pnlMonitor = new PNLMonitor(transFac);

		OrderEventListener oel = new OrderEventListener();
		String l = br.readLine();

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

		while (l != null) {
			String[] p = l.split(",");
			String inst = p[0];
			TimeStamp ts = new TimeStamp(sdf.parse(p[1]));
			String dir = p[2];
			Double price = Double.parseDouble(p[4]);
			Double quantity = Double.parseDouble(p[3]);
			prc.execution(ts, inst, price, (dir.startsWith("B") ? 1 : -1) * quantity);
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
		// //////////////
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

		// calculate the cash positions ... beware: for currency pairs, we have
		// two cash positions!
		TSContainer2 posDeltaOverTime = oel.getChangeOverTime();
		TSContainer2 executionPricesOverTime = oel.getExecutionOverTime();
		TSContainer2 cashPositionsOverTime = new TSContainer2("CASH", new ArrayList<String>(),
				new ArrayList<TypedColumn>());

		//
		List<String> instruments = posDeltaOverTime.getColumnHeaders();
		for (String tdiId : instruments) {
			// skip the instrument lookup for now - as this is mostly a custom
			// development at the moment, I just assume we have only FX.
			String base, quotee;
			if (tdiId.length() == 6) {
				base = tdiId.substring(0, 3);
				quotee = tdiId.substring(3);
				System.out.println("Calculating cash position for " + base + "/" + quotee);
				DoubleColumn posDeltaColumn = (DoubleColumn) posDeltaOverTime.getColumn(tdiId);
				DoubleColumn execPriceColumn = (DoubleColumn) executionPricesOverTime.getColumn(tdiId);

				//
				List<TimeStamp> timeStamps = posDeltaOverTime.getTimeStamps();
				cashPositionsOverTime.setTimeStamps(timeStamps);

				// check if we already have the cash columns
				addColAndInit(cashPositionsOverTime, base, timeStamps);
				addColAndInit(cashPositionsOverTime, quotee, timeStamps);

				// get the two columns and keep track of the stuff.
				DoubleColumn baseCol = (DoubleColumn) cashPositionsOverTime.getColumn(base);
				DoubleColumn quoteeCol = (DoubleColumn) cashPositionsOverTime.getColumn(quotee);
				for (int i = 0; i < timeStamps.size(); i++) {
					//
					Double posDelta = posDeltaColumn.get(i);
					if (posDelta != null) {
						//
						Double execPrice = execPriceColumn.get(i);
						Double presentBasePos = baseCol.get(i);
						Double presentQuoteePos = quoteeCol.get(i);
						//
						presentBasePos += posDelta;
						presentQuoteePos -= posDelta * execPrice;
						//
						baseCol.set(i, presentBasePos);
						quoteeCol.set(i, presentQuoteePos);
					}
					//
				}
			}
		}

		fout = new FileOutputStream(targetFolder + File.separator + "cash_positions.csv");
		CSVExporter c = new CSVExporter(fout, cashPositionsOverTime);
		c.write();
		fout.close();
		
		// dump out the fees.
		if (oel.getFeeCalculator() != null) {
			//
			fout = new FileOutputStream(targetFolder + File.separator + "fees.csv");
			c = new CSVExporter(fout, oel.getFeeCalculator().feesSeries());
			c.write();
			fout.close();
		}

		// generate a position chart.

		ChartUtilities.saveChartAsPNG(new File(targetFolder + File.separator + "position.png"),
				ChartUtils.getStepChart("Position", oel.getPositionOverTime()), 800, 600);

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
		h.genReport(new AlgoConfig[] {}, oel, pnlMonitor, null);

	}

	private void addColAndInit(TSContainer2 container, String colName, List<TimeStamp> timeStamps) {
		if (!container.getColumnHeaders().contains(colName)) {
			DoubleColumn col = new DoubleColumn();
			container.addColumn(colName, col);
			col.clear();
			// initialize the column.
			for (TimeStamp ts : timeStamps)
				col.add(0.0);
		}
	}

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws Exception {
		new TransactionInputToReport("/home/ustaudinger/MOSTRA_ALL_Trades.csv", null);

	}

}
