package com.activequant.trading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.domainmodel.streaming.PNLChangeEvent;
import com.activequant.interfaces.trading.IRiskCalculator;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.trading.datamodel.InstrumentTable;
import com.activequant.trading.datamodel.QuoteTable;

/**
 * should be decoupled from abstractTSBase. 
 * Should send out risk events. 
 * Abstract TS Base should manage risk events. 
 * 
 * @author GhostRider
 *
 */
public class PositionRiskCalculator implements IRiskCalculator {

	private AbstractTSBase tsBase;
	private Map<String, Double> positions = new HashMap<String, Double>();
	private Map<String, Double> lastValPrices = new HashMap<String, Double>();
	private Map<String, Double> avgPrices = new HashMap<String, Double>();
	private IPublisher riskDataPublisher = null;
	private List<Long> lastEvalMinutes = new ArrayList<Long>();

	//
	public PositionRiskCalculator(AbstractTSBase tsBase) {
		this.tsBase = tsBase;
	}

	@Override
	public void setTransportFactory(ITransportFactory transportFactory) {
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

	public void setPosition(String tid, double price, double quantity){
		positions.put(tid,  quantity);
		lastValPrices.put(tid, price);
		avgPrices.put(tid, price);
	}
	
	private PNLChangeEvent pnl(TimeStamp ts, String tid, double price, double posChange) {
		PNLChangeEvent pce = null; 
		Double formerPos = positions.get(tid);
		 
		Double lastValuationPrice = lastValPrices.get(tid);
		Double avgPrice = avgPrices.get(tid);
		if (formerPos == null) {
			formerPos = 0.0;
			lastValuationPrice = 0.0;
		}
		Double newPos = formerPos + posChange;
		if (formerPos != 0.0) {
			// revalue the former position.
			double pnlChange = (price - lastValuationPrice) * formerPos;
			double unrealizedPnl = (price - avgPrice) * formerPos; 
			if(newPos==0.0)
				unrealizedPnl = 0.0; 
			pce = new PNLChangeEvent(ts, tid, pnlChange, unrealizedPnl);
			try {
				riskDataPublisher.send(pce);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(posChange!=0.0)
			positions.put(tid, newPos);
		lastValPrices.put(tid, price);
		return pce; 
	}

	
	private boolean recalc(int rowIndex){
		while(lastEvalMinutes.size()<(rowIndex+1)){
			lastEvalMinutes.add(0L);
		}
		long lastEval = lastEvalMinutes.get(rowIndex); 
		long currentMinute = tsBase.getCurrentTimeSlot(); 
		if(lastEval!=currentMinute){
			lastEvalMinutes.set(rowIndex, currentMinute);
			return true; 
		}
		return false; 
	}
	
	
	@Override
	public PNLChangeEvent pricesUpdated(int rowIndex) {
		PNLChangeEvent pce = null;  
		if (recalc(rowIndex)) {
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
		}
		return null; 
	}
	
	

}
