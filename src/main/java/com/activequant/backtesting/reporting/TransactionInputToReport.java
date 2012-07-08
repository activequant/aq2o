package com.activequant.backtesting.reporting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.jfree.chart.ChartUtilities;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.IArchiveReader;
import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.backtesting.ArchiveStreamToOHLCIterator;
import com.activequant.backtesting.FastStreamer;
import com.activequant.backtesting.IBFXFeeCalculator;
import com.activequant.backtesting.IFeeCalculator;
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

public class TransactionInputToReport {

	private String fileName;
	private String targetFolder = "./reports2";
	private String reportCurrency;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public TransactionInputToReport(String transactionsFile, String configFile, String tgt, String archiveServer) throws Exception {
		if (tgt != null)
			targetFolder = tgt;
		this.fileName = transactionsFile;
		Properties properties = new Properties();
		if(configFile!=null)
			properties.load(new FileInputStream(configFile));
		
		IArchiveFactory archFac = new HBaseArchiveFactory(archiveServer);
		IArchiveReader archReader = archFac.getReader(TimeFrame.MINUTES_1);
		
		System.out.println("ArchiveReader fetched.");
		String instrumentsInSim = properties.getProperty("instrumentsInSim", "PI_EURUSD,PI_EURGBP");
		TimeFrame timeFrame = TimeFrame.valueOf(properties.getProperty("resolution", "MINUTES_1"));
		String simStart = properties.getProperty("simStart", "20120101");
		String simEnd = properties.getProperty("simStart", "20120201");
		// 
		
		
		@SuppressWarnings("rawtypes")
		List<StreamEventIterator> streamIters = new ArrayList<StreamEventIterator>();
		
		// initialize the market data replay streams. 
		String[] tids = instrumentsInSim.split(",");
		for(String tid : tids){
			ArchiveStreamToOHLCIterator a = new ArchiveStreamToOHLCIterator(tid, TimeFrame.MINUTES_1, new TimeStamp(sdf.parse(simStart)),new TimeStamp(sdf.parse(simEnd)), archReader);
			// no shifting, as PiTrading's candles are timestamped with end of minute instead of beginning of minute like proper 
			a.setOffset(0L);
			streamIters.add(a);
		}
		
		// initialize the transaction file streamer. 
		TransactionFileStreamIterator tfsi = new TransactionFileStreamIterator(transactionsFile);
		streamIters.add(tfsi);
		
		
		// 
		// initialize the fast streamer  
		@SuppressWarnings("unchecked")
		FastStreamer fs = new FastStreamer(streamIters.toArray(new StreamEventIterator[]{}));
		
		
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

		
		//////////////////// 
		
		while(fs.moreDataInPipe()){
			StreamEvent se = fs.getOneFromPipes();			
			System.out.println(se.getTimeStamp().getDate());
			if(se instanceof OrderFillEvent){
				OrderFillEvent ofe = (OrderFillEvent)se;
				feeCalculator.updateRefRate(ofe.getOptionalInstId(), ofe.getFillPrice());
				// order event listener also holds the fee calculator
				oel.eventFired((OrderFillEvent)se);
				prc.execution(ofe.getCreationTimeStamp(), "PI_"+ofe.getOptionalInstId(), 
						ofe.getFillPrice(), (ofe.getSide().startsWith("B") ? 1 : -1) * ofe.getFillAmount());
			}
			else if(se instanceof OHLCV){
				OHLCV o = (OHLCV) se; 
				System.out.println(o.toString());
				
				// use a zero-change execution to push in the price. 
				prc.execution(o.getTimeStamp(), o.getMdiId(), 
						o.getClose(), 0.0);
				// 
			}
		}
		
		
		System.out.println("*************** REPLAY DONE ");
		/////////////////////
		
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

		
		TSContainerMethods tcm = new TSContainerMethods();
		pnlContainer = tcm.overwriteNull(pnlContainer);
		pnlContainer = tcm.overwriteNull(pnlContainer, 0.0);
		bs.calcPNLStats(pnlContainer);
		
		TSContainer2 posOverTime = oel.getPositionOverTime();
		posOverTime = tcm.overwriteNull(posOverTime);
		posOverTime = tcm.overwriteNull(posOverTime, 0.0);
		bs.calcPosStats(posOverTime);
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

		// run R. 
		new RExec("/home/ustaudinger/work/activequant/trunk/src/main/resources/r/perfreport.r", 
				new String[]{targetFolder+"pnl.csv", targetFolder+"cash_positions.csv", targetFolder});
		
		
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
		new TransactionInputToReport("/home/ustaudinger/work/activequant/trunk/src/test/resources/transactions/transactions.csv", null, "/home/ustaudinger/work/activequant/trunk/src/test/resources/transactions/", "reporting.pecoracapital.com");
		//new TransactionInputToReport(args[0], null, args[1], args[2]);

	}

}
