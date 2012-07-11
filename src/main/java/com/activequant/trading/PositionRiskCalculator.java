package com.activequant.trading;

import java.util.HashMap;
import java.util.Map;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.exceptions.TransportException;
import com.activequant.tools.streaming.PNLChangeEvent;
import com.activequant.trading.datamodel.InstrumentTable;
import com.activequant.trading.datamodel.QuoteTable;
import com.activequant.transport.ETransportType;
import com.activequant.transport.IPublisher;
import com.activequant.transport.ITransportFactory;

public class PositionRiskCalculator implements IRiskCalculator {

	private AbstractTSBase tsBase;
	private Map<String, Double> positions = new HashMap<String, Double>();
	private Map<String, Double> lastValPrices = new HashMap<String, Double>();
	private ITransportFactory transportFactory;
	private IPublisher riskDataPublisher = null;
	private long lastMinute = 0L;

	//
	public PositionRiskCalculator(AbstractTSBase tsBase) {
		this.tsBase = tsBase;

	}

	@Override
	public void setTransportFactory(ITransportFactory transportFactory) {
		this.transportFactory = transportFactory;
		try {
			riskDataPublisher = transportFactory.getPublisher(ETransportType.RISK_DATA.toString());
		} catch (TransportException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This one can be used to make the pos risk calculator recompute pnl by setting a price with zero quantity
	 */
	public PNLChangeEvent execution(TimeStamp ts, String tid, double price, double quantity) {
		return pnl(ts, tid, price, quantity);
	}

	private PNLChangeEvent pnl(TimeStamp ts, String tid, double price, double posChange) {
		PNLChangeEvent pce = null; 
		Double formerPos = positions.get(tid);
		Double lastValuationPrice = lastValPrices.get(tid);
		if (formerPos == null) {
			formerPos = 0.0;
			lastValuationPrice = 0.0;
		}
		if (formerPos != 0.0) {
			// revalue the former position.
			double pnlChange = (price - lastValuationPrice) * formerPos;
			pce = new PNLChangeEvent(ts, tid, pnlChange);
			try {
				riskDataPublisher.send(pce);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(posChange!=0.0)
			positions.put(tid, formerPos + posChange);
		lastValPrices.put(tid, price);
		return pce; 
	}

	@Override
	public PNLChangeEvent pricesUpdated(int rowIndex) {
		PNLChangeEvent pce = null; 
		if (tsBase.getCurrentMinute() != lastMinute) {
			String mdiId = (String) tsBase.getQuoteTable().getCell(rowIndex, QuoteTable.Columns.INSTRUMENTID.colIdx());
			// get the tradeable ID.
			int rowIndex2 = tsBase.getInstrumentTable().getRowIndexOf(mdiId);
			String tid = (String) tsBase.getInstrumentTable().getCell(rowIndex2,
					InstrumentTable.Columns.TRADEABLEID.colIdx());
			Double position = positions.get(tid);
			if (position != null && position != 0.0) {
				Double valPrice = null;
				if (position > 0.0)
					valPrice = (Double) tsBase.getQuoteTable().getCell(rowIndex, QuoteTable.Columns.BID.colIdx());
				else
					valPrice = (Double) tsBase.getQuoteTable().getCell(rowIndex, QuoteTable.Columns.ASK.colIdx());
				//
				if (valPrice != null)
					pce = pnl(tsBase.currentTime, tid, valPrice, 0.0);
			}
			lastMinute = tsBase.getCurrentMinute();
		}
		return null; 
	}
	
	

}
