package com.activequant.backtesting.reporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jfree.chart.ChartUtilities;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.IArchiveReader;
import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.backtesting.ArchiveStreamToOHLCIterator;
import com.activequant.backtesting.FastStreamer;
import com.activequant.backtesting.IBFXFeeCalculator;
import com.activequant.backtesting.OrderEventListener;
import com.activequant.domainmodel.AlgoConfig;
import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.timeseries.CSVExporter;
import com.activequant.timeseries.ChartUtils;
import com.activequant.timeseries.DoubleColumn;
import com.activequant.timeseries.TSContainer2;
import com.activequant.timeseries.TSContainerMethods;
import com.activequant.timeseries.TypedColumn;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.tools.streaming.StreamEventIterator;
import com.activequant.trading.PositionRiskCalculator;
import com.activequant.transport.ITransportFactory;
import com.activequant.transport.memory.InMemoryTransportFactory;
import com.activequant.utils.CsvMapWriter;

/**
 * This is a very narrow implementation at the moment. As it is written in a
 * client project, this might or might not work for your use case.
 * 
 * @author GhostRider
 * 
 */
public class TransactionInputToReport {

	private String fileName;
	private String targetFolder = "./reports2";
	private String reportCurrency;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public TransactionInputToReport(String transactionsFile, String configFile, String tgt, String archiveServer)
			throws Exception {
		if (tgt != null)
			targetFolder = tgt;
		this.fileName = transactionsFile;
		Properties properties = new Properties();
		if (configFile != null)
			properties.load(new FileInputStream(configFile));

		IArchiveFactory archFac = new HBaseArchiveFactory(archiveServer);
		IArchiveReader archReader = archFac.getReader(TimeFrame.MINUTES_1);

		System.out.println("ArchiveReader fetched.");
		String instrumentsInSim = properties.getProperty("instrumentsInSim", "PI_EURUSD,PI_EURGBP");
		TimeFrame timeFrame = TimeFrame.valueOf(properties.getProperty("resolution", "HOURS_1"));
		String simStart = properties.getProperty("simStart", "20120101");
		String simEnd = properties.getProperty("simStart", "20120205");

		//
		TimeStamp startTimeStamp = new TimeStamp(sdf.parse(simStart));
		TimeStamp endTimeStamp = new TimeStamp(sdf.parse(simEnd));

		//
		TSContainerMethods tcm = new TSContainerMethods();

		@SuppressWarnings("rawtypes")
		List<StreamEventIterator> streamIters = new ArrayList<StreamEventIterator>();

		// initialize the market data replay streams.
		String[] tids = instrumentsInSim.split(",");
		for (String tid : tids) {
			if(!tid.startsWith("PI_"))tid = "PI_"+tid;
			ArchiveStreamToOHLCIterator a = new ArchiveStreamToOHLCIterator(tid, TimeFrame.MINUTES_1, startTimeStamp,
					endTimeStamp, archReader);
			// no shifting, as PiTrading's candles are timestamped with end of
			// minute instead of beginning of minute like proper
			a.setOffset(0L);
			streamIters.add(a);
		}

		// initialize the transaction file streamer.
		TransactionFileStreamIterator tfsi = new TransactionFileStreamIterator(transactionsFile);
		streamIters.add(tfsi);

		//
		// initialize the fast streamer
		@SuppressWarnings("unchecked")
		FastStreamer fs = new FastStreamer(streamIters.toArray(new StreamEventIterator[] {}));

		//
		CSVFileFillExporter fillExporter = new CSVFileFillExporter();
		//
		ITransportFactory transFac = new InMemoryTransportFactory();
		//
		// pos risk calc listens to executions and price events.
		PositionRiskCalculator prc = new PositionRiskCalculator(null);
		prc.setTransportFactory(transFac);
		// pnl monitor listens to risk events .. these come from the position
		// risk calculator
		PNLMonitor pnlMonitor = new PNLMonitor(transFac, timeFrame);

		OrderEventListener oel = new OrderEventListener();
		IBFXFeeCalculator feeCalculator = new IBFXFeeCalculator();
		oel.setFeeCalculator(feeCalculator);

		// //////////////////

		while (fs.moreDataInPipe()) {
			StreamEvent se = fs.getOneFromPipes();
			System.out.println(se.getTimeStamp().getDate());
			if (se instanceof OrderFillEvent) {
				OrderFillEvent ofe = (OrderFillEvent) se;
				feeCalculator.updateRefRate(ofe.getOptionalInstId(), ofe.getFillPrice());
				// order event listener also holds the fee calculator
				oel.eventFired((OrderFillEvent) se);
				prc.execution(ofe.getCreationTimeStamp(), "PI_" + ofe.getOptionalInstId(), ofe.getFillPrice(), (ofe
						.getSide().startsWith("B") ? 1 : -1) * ofe.getFillAmount());
			} else if (se instanceof OHLCV) {
				OHLCV o = (OHLCV) se;
				System.out.println(o.toString());

				// use a zero-change execution to push in the price.
				prc.execution(o.getTimeStamp(), o.getMdiId(), o.getClose(), 0.0);
				//
			}
		}

		System.out.println("*************** REPLAY DONE ");
		// ///////////////////

		TSContainer2 tsc = pnlMonitor.getCumulatedTSContainer();
		// //////////////
		new File(targetFolder).mkdirs();
		fillExporter.export(targetFolder, oel.getFillEvents());
		// generate PNL report
		TSContainer2 pnlContainer = pnlMonitor.getCumulatedTSContainer();
		FileOutputStream fout;

		fout = new FileOutputStream(targetFolder + File.separator + "pnl.csv");
		CSVExporter c = new CSVExporter(fout, pnlContainer);
		c.write();
		fout.close();

		// generate a PNL chart.
		ChartUtilities.saveChartAsPNG(new File(targetFolder + File.separator + "pnl.png"), pnlMonitor.getStaticChart(),
				800, 600);

		// calculate the cash positions ... beware: for currency pairs, we have
		// two cash positions!
		TSContainer2 posDeltaOverTime = oel.getChangeOverTime();
		TSContainer2 executionPricesOverTime = oel.getExecutionOverTime();
		TSContainer2 deltaCashPositionsOverTime = calcCashPositions(posDeltaOverTime, executionPricesOverTime);

		fout = new FileOutputStream(targetFolder + File.separator + "cash_positions_delta.csv");
		c = new CSVExporter(fout, deltaCashPositionsOverTime);
		c.write();
		fout.close();

		TSContainer2 inflatedCashPositionSeries = resampleSeries(deltaCashPositionsOverTime, timeFrame, startTimeStamp,
				endTimeStamp);
		// have to cumsum
		for (int i = 0; i < inflatedCashPositionSeries.getNumColumns(); i++) {
			DoubleColumn dc = (DoubleColumn) inflatedCashPositionSeries.getColumns().get(i);
			dc = dc.cumsum();
			inflatedCashPositionSeries.getColumns().set(i, dc);
		}

		inflatedCashPositionSeries = tcm.overwriteNull(inflatedCashPositionSeries);
		inflatedCashPositionSeries = tcm.overwriteNull(inflatedCashPositionSeries, 0.0);
		fout = new FileOutputStream(targetFolder + File.separator + "inflated_cash_positions.csv");
		c = new CSVExporter(fout, inflatedCashPositionSeries);
		c.write();
		fout.close();

		// create borrowing and lending payments.
		TSContainer2 borrowingAndLendingContainer = calcInterestChanges(startTimeStamp, endTimeStamp, timeFrame,
				inflatedCashPositionSeries);
		fout = new FileOutputStream(targetFolder + File.separator + "interest.csv");
		c = new CSVExporter(fout, borrowingAndLendingContainer);
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

		fout = new FileOutputStream(targetFolder + File.separator + "positions.csv");
		c = new CSVExporter(fout, oel.getPositionOverTime());
		c.write();
		fout.close();

		TSContainer2 inflatedPositionSeries = resampleSeries(oel.getPositionOverTime(), timeFrame, startTimeStamp,
				endTimeStamp);
		inflatedPositionSeries = tcm.overwriteNull(inflatedPositionSeries);
		inflatedPositionSeries = tcm.overwriteNull(inflatedPositionSeries, 0.0);
		fout = new FileOutputStream(targetFolder + File.separator + "inflated_positions.csv");
		c = new CSVExporter(fout, inflatedPositionSeries);
		c.write();
		fout.close();

		// calculate some statistics.
		BacktestStatistics bs = new BacktestStatistics();
		bs.setReportId(new SimpleDateFormat("yyyyMMdd").format(new Date()));

		pnlContainer = tcm.overwriteNull(pnlContainer);
		pnlContainer = tcm.overwriteNull(pnlContainer, 0.0);
		bs.calcPNLStats(pnlContainer);

		TSContainer2 posOverTime = oel.getPositionOverTime();
		posOverTime = tcm.overwriteNull(posOverTime);
		posOverTime = tcm.overwriteNull(posOverTime, 0.0);
		bs.calcPosStats(posOverTime);
		bs.populateOrderStats(oel);

		// dump the stats
		fout = new FileOutputStream(targetFolder + File.separator + "statistics.csv");
		new CsvMapWriter().write(bs.getStatistics(), fout);
		fout.close();

		// generate the html report.
		File directory = new File(".");
		HTMLReportGen h = new HTMLReportGen(targetFolder, directory.getAbsolutePath() + "/templates");
		h.genReport(new AlgoConfig[] {}, oel, pnlMonitor, null);

		// run R.
		new RExec(directory.getAbsolutePath() + "/r/perfreport.r", new String[] { targetFolder + "pnl.csv",
				targetFolder + "inflated_cash_positions.csv", targetFolder });

	}

	private TSContainer2 resampleSeries(TSContainer2 container, TimeFrame timeFrame, TimeStamp startTimeStamp,
			TimeStamp endTimeStamp) {
		// resample the position series.
		TSContainer2 resampled = getEmptyContainer(container.getColumnHeaders(), startTimeStamp, endTimeStamp,
				timeFrame);
		List<String> headers = container.getColumnHeaders();
		for (int i = 0; i < container.getTimeStamps().size(); i++) {
			TimeStamp ts = container.getTimeStamps().get(i);
			Object[] o = container.getRow(ts);
			for (int j = 0; j < o.length; j++) {
				if (o[j] != null) {
					resampled.setValue(headers.get(j), ts, (Double) o[j]);
				}
			}
		}
		return resampled;
	}

	private TSContainer2 calcCashPositions(TSContainer2 posDeltaOverTime, TSContainer2 executionPricesOverTime) {
		TSContainer2 cashPositionsOverTime = new TSContainer2("CASH", new ArrayList<String>(),
				new ArrayList<TypedColumn>());
		// calculating cash positions.
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
		return cashPositionsOverTime;
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

	private class ChargedInterestRates {
		private Map<String, Double[]> rates = new HashMap<String, Double[]>();

		ChargedInterestRates() throws IOException {
			File directory = new File(".");
			File f1 = new File(directory.getAbsolutePath() + "/ib/ib_charged_9_july_2012.csv");
			BufferedReader br = new BufferedReader(new FileReader(f1));
			String l = br.readLine();
			// skipping header.
			l = br.readLine();
			while (l != null) {
				String[] parts = l.split(",");
				String cncy = parts[0];
				Double[] data = new Double[8];
				for (int i = 1; i < parts.length; i++) {
					if (parts[i].length() > 0) {
						data[i - 1] = Double.parseDouble(parts[i]);
					}
				}
				rates.put(cncy, data);
				l = br.readLine();
			}
		}

		Double getOvernightChange(String currency, Double balance) {
			if (rates.containsKey(currency)) {
				Double[] d = rates.get(currency);
				double days = d[0];
				double refRate = 0.0;
				if (balance < d[1])
					refRate = d[2];
				if (balance >= d[1])
					refRate = d[3];
				if (balance >= d[4])
					refRate = d[5];
				if (d[6] != null && balance >= d[6])
					refRate = d[7];

				return balance * (refRate / 100.0) / days;
			} else
				return 0.0;
		}

	}

	private class EarnedInterestRates {
		private Map<String, Double[]> rates = new HashMap<String, Double[]>();

		EarnedInterestRates() throws IOException {
			File directory = new File(".");
			File f1 = new File(directory.getAbsolutePath() + "/ib/ib_earned_9_july_2012.csv");
			BufferedReader br = new BufferedReader(new FileReader(f1));
			String l = br.readLine();
			// skipping header.
			l = br.readLine();
			while (l != null) {
				String[] parts = l.split(",");
				String cncy = parts[0];
				Double[] data = new Double[7];
				for (int i = 1; i < parts.length; i++) {
					if (parts[i].length() > 0) {
						data[i - 1] = Double.parseDouble(parts[i]);
					}
				}
				rates.put(cncy, data);
				l = br.readLine();
			}
		}

		Double getOvernightChange(String currency, Double balance) {
			if (rates.containsKey(currency)) {
				Double[] d = rates.get(currency);
				double days = d[0];
				double refRate = 0.0;
				if (balance >= d[1])
					refRate = d[2];
				if (balance >= d[3])
					refRate = d[4];
				if (d[5] != null && balance >= d[5])
					refRate = d[6];

				return balance * (refRate / 100.0) / days;
			} else
				return 0.0;
		}

	}

	private TSContainer2 getEmptyContainer(List<String> headers, TimeStamp startTimeStamp, TimeStamp endTimeStamp,
			TimeFrame reportResolution) {
		List<TypedColumn> columns = new ArrayList<TypedColumn>();
		for (int i = 0; i < headers.size(); i++)
			columns.add(new DoubleColumn());

		TSContainer2 tsc = new TSContainer2("-", headers, columns);
		tsc.setResolutionInNanoseconds(reportResolution.getNanoseconds());

		List<TimeStamp> markStamps = new TSContainerMethods().getListOfTimeStamps(startTimeStamp, endTimeStamp,
				reportResolution);
		Double[] naRow = new Double[columns.size()];
		for (TimeStamp ts : markStamps) {
			tsc.setRow(ts, naRow);
		}
		return tsc;
	}

	private TSContainer2 calcInterestChanges(TimeStamp startTimeStamp, TimeStamp endTimeStamp,
			TimeFrame reportResolution, TSContainer2 cashPositionsOverTime) throws IOException {
		// load the borrowing and lending reference rates.
		ChargedInterestRates chargedIr = new ChargedInterestRates();
		EarnedInterestRates earnedIr = new EarnedInterestRates();
		// iterate over the days.
		List<TypedColumn> columns = new ArrayList<TypedColumn>();
		for (int i = 0; i < cashPositionsOverTime.getColumns().size(); i++)
			columns.add(new DoubleColumn());
		TSContainer2 tsc = new TSContainer2("INTEREST", cashPositionsOverTime.getColumnHeaders(), columns);
		// create a sequence of days between start and end timestamp in the
		// report resolution

		List<TimeStamp> markStamps = new TSContainerMethods().getListOfTimeStamps(startTimeStamp, endTimeStamp,
				reportResolution);
		// ib specific: daily accruals.
		int currentDay = Integer.parseInt(sdf.format(markStamps.get(0).getCalendar().getTime()));
		Double[] zeroRow = new Double[columns.size()];
		for (int i = 0; i < zeroRow.length; i++)
			zeroRow[i] = 0.0;
		for (TimeStamp ts : markStamps) {
			tsc.setRow(ts, zeroRow);

			int day = Integer.parseInt(sdf.format(ts.getCalendar().getTime()));
			if (day != currentDay) {
				int indexBefore = cashPositionsOverTime.getIndexBefore(ts);
				if (indexBefore > -1) {
					// ok, new day, let's book the accruals, based on
					// yesterday's cash positions.
					for (int i = 0; i < cashPositionsOverTime.getColumns().size(); i++) {
						DoubleColumn dc = (DoubleColumn) cashPositionsOverTime.getColumns().get(i);
						Double val = dc.get(indexBefore);
						String cncy = cashPositionsOverTime.getColumnHeaders().get(i);
						if (val != null) {
							if (val < 0.0) {
								Double charge = chargedIr.getOvernightChange(cncy, val);
								tsc.setValue(cncy, ts, charge);
							} else if (val > 0.0) {
								Double earnings = earnedIr.getOvernightChange(cncy, val);
								tsc.setValue(cncy, ts, earnings);
							}
						}
					}
				}
				currentDay = day;
			}
		}
		//
		return tsc;

	}

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws Exception {
		/*new TransactionInputToReport(
				"/home/ustaudinger/work/activequant/trunk/src/test/resources/transactions/transactions.csv", null,
				"/home/ustaudinger/work/activequant/trunk/src/test/resources/transactions/",
				"reporting.pecoracapital.com");*/
		new TransactionInputToReport(args[0], args[1], args[2], args[3]);

	}

}
