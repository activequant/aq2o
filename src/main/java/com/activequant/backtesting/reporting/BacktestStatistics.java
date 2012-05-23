package com.activequant.backtesting.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.activequant.backtesting.OrderEventListener;
import com.activequant.timeseries.DoubleColumn;
import com.activequant.timeseries.TSContainer2;

/**
 * deprecated right from the start. Will become a persistent entity soon.
 * 
 * @author GhostRider
 * 
 */
public class BacktestStatistics {

	private String reportId;
	private List<String> instrumentIDs;
	final private Map<String, Object> statistics = new HashMap<String, Object>();

	public void calcPNLStats(TSContainer2 cumPnlSeries) {
		instrumentIDs = new ArrayList<String>();
		for (int i = 0; i < cumPnlSeries.getNumColumns(); i++) {
			String header = cumPnlSeries.getColumnHeaders().get(i);
			instrumentIDs.add(header);
			Double maxVal = (Double) cumPnlSeries.getMax(header);
			String varName = header + ".MAXPNL";
			statistics.put(varName, maxVal);

			DoubleColumn dc = (DoubleColumn) cumPnlSeries.getColumn(header);
			Double finalPnl = dc.get(dc.size() - 1);
			statistics.put(header + ".FINALPNL", finalPnl);
		}
	}

	public void calcPosStats(TSContainer2 positionSeries) {

	}

	public void calcRiskStats(TSContainer2 valuatedPositionSeries) {

	}

	public void populateOrderStats(OrderEventListener oel) {
		for (String id : instrumentIDs) {

			statistics.put(id + ".TOTALPLACED", oel.getPlaced().get(id));
			statistics.put(id + ".TOTALFILLS", oel.getFills().get(id));
			statistics.put(id + ".TOTALORDERUPDS", oel.getUpdates().get(id));
			statistics.put(id + ".TOTALORDERCNCL", oel.getCancellations().get(id));

			if (oel.getFills().get(id) != null) {
				Double finalPnl = (Double) statistics.get(id + ".FINALPNL");
				statistics.put(id + ".PNLPERTRADE", finalPnl / oel.getFills().get(id));
			}

		}
	}

	public Map<String, Object> getStatistics() {
		return statistics;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public List<String> getInstrumentIDs() {
		return instrumentIDs;
	}

}
