package com.activequant.backtesting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.timeseries.TSContainer2;
import com.activequant.timeseries.TypedColumn;

/**
 * http://www.interactivebrokers.com/de/accounts/fees/commission.php?ib_entity=
 * de
 * 
 * Makes a flat commission structure - (no three tier structure).
 * 
 * Highly FX specific at the moment.
 * 
 * @author GhostRider
 * 
 */
public class IBFXFeeCalculator implements IFeeCalculator {

	private Map<String, Double> conversionSheet = new HashMap<String, Double>();
	private String accountBaseCurrency = "USD";
	private double minimumPerOrder = 2.5;
	private double commissionBps = 0.2; //
	private double tickSizeAcctCurrency = 0.0001;
	private TSContainer2 feeSeries = new TSContainer2("FEES", new ArrayList<String>(), new ArrayList<TypedColumn>());
	private Map<String, Double> runningPositions = new HashMap<String, Double>();
	private Map<String, Double> avgEntryPrice = new HashMap<String, Double>();
	private Logger log = Logger.getLogger(IBFXFeeCalculator.class);

	// very dirty and against engineering ethics: nonreusable code below.
	private final List<String> rows = new ArrayList<String>();
	private DecimalFormat dcf = new DecimalFormat("#.######");

	public IBFXFeeCalculator() {
		// dump a row.
		String row = "REFORDERID;TS_IN_NANOSECONDS;INSTID;";
		row += "SIDE;Q;PX;CONVERSION_RATE_TO_USD;TRADED_VAL_IN_QUOTEE;TRADED_VAL_IN_USD;COMMISSION";
		rows.add(row);
	}

	public void updateRefRate(String id, Double ref) {
		conversionSheet.put(id, ref);
	}

	public Double getCurrentPos(String tid){
		if(runningPositions.containsKey(tid))
			return runningPositions.get(tid);
		return 0.0; 
	}
	
	public Double getAvgPx(String tid){
		if(avgEntryPrice.containsKey(tid))
			return avgEntryPrice.get(tid);
		return 0.0; 
	}
	
	@Override
	public void track(OrderEvent orderEvent) {
		if (orderEvent instanceof OrderFillEvent) {
			//
			OrderFillEvent ofe = (OrderFillEvent) orderEvent;
			//
			String tid = ofe.getOptionalInstId();

			double volume = ofe.getFillAmount();
			if (volume == 0.0)
				return;
			// 
			double execPrice = ofe.getFillPrice();
			//
			double tradedValueInQuotee = volume * execPrice;
			// 
			if(tid.startsWith("PI_"))
				tid = tid.substring(3);
			String base = tid.substring(0, 3);
			String quotee = tid.substring(3);
			// 
			tid="PI_" + tid; 
			
			// doing equally weighted average pricing. 
			Double currentPos = getCurrentPos(tid);
			Double avgPx = getAvgPx(tid);
			
			// 
			double signedVolume = volume;
			
			if(ofe.getSide().startsWith("S")){
				signedVolume = - signedVolume; 
			}		
			log.info(tid+": signed volume: " + signedVolume);
			
			
			// 
			Double closingTradePnl = 0.0; 
			if(Math.signum(signedVolume) == Math.signum(currentPos)){
				
				// increase of position  
				Double newPos = currentPos + signedVolume; 
				Double newAvgPx = Math.abs(((currentPos * avgPx) + (signedVolume*execPrice))/newPos);
				avgPx = newAvgPx; 
				currentPos = newPos; 
				// 
				this.avgEntryPrice.put(tid,  newAvgPx);
				this.runningPositions.put(tid,  currentPos);
				//
				log.info("New avg entry price and running position for " + tid+": " + newAvgPx+"/"+currentPos);
				
				
			}
			else
			{
				if(currentPos!=0.0){
					// decrease of position. 
					// By using equally weighted inventory (contrary to FIFO and LIFO), we can keep the average price constant.
					Double newPos = currentPos + signedVolume;
					currentPos = newPos; 
					this.runningPositions.put(tid,  currentPos);
					if(Math.signum(signedVolume)==1.0){
						// means we were in a short position and are reducing it. 
						closingTradePnl = (avgPx - execPrice) * volume; 
					}
					else{
						// means we were in a long position and are reducing it. 
						closingTradePnl = (execPrice - avgPx) * volume;
					}
				}
				else{
					currentPos = signedVolume; 
					this.avgEntryPrice.put(tid, avgPx);
					this.runningPositions.put(tid,  currentPos);
					log.info("New avg entry price and running position for " + tid+": " + avgPx+"/"+currentPos);
				}
			}			
			
			// 
			double conversionRate = 1.0;
			if (base.equals("USD")) {
				conversionRate = 1.0 / execPrice;
			} else if (quotee.equals("USD")) {
				conversionRate = 1.0;
			} else {
				conversionRate = getConversionRate(base, quotee, execPrice);
			}
			
			//
			double tradedValueInUsd = conversionRate * tradedValueInQuotee;

			//
			double commission = Math.max((0.2 * tickSizeAcctCurrency * tradedValueInUsd), 2.50);
						
			// track it.			
			Double existingFees = (Double) feeSeries.getValue(tid, ofe.getCreationTimeStamp());
			if(existingFees==null)existingFees = 0.0; 
			feeSeries.setValue(tid, ofe.getCreationTimeStamp(), commission+existingFees);

			// dump a row.
			String row = ofe.getRefOrderId() + ";" + ofe.getCreationTimeStamp().getNanoseconds() + ";"
					+ ofe.getOptionalInstId() + ";";
			row += ofe.getSide() + ";" + dcf.format(ofe.getFillAmount()) + ";" + dcf.format(ofe.getFillPrice()) + ";";
			row += dcf.format(conversionRate) + ";" + dcf.format(tradedValueInQuotee) + ";"
					+ dcf.format(tradedValueInUsd) + ";" + dcf.format(commission)+";"+dcf.format(avgPx)+";"+dcf.format(currentPos)+";"+dcf.format(closingTradePnl);
			rows.add(row);

		}
	}

	/**
	 * iterates over the quote sheets to find the first matching pair to convert
	 * based on base or quotee to USD.
	 * 
	 * @param base
	 * @param quotee
	 */
	private double getConversionRate(String base, String quotee, Double refQuote) {
		//
		double ret = 1.0;
		Iterator<Entry<String, Double>> it = conversionSheet.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Double> entry = it.next();
			String pair = entry.getKey();
			Double rate = entry.getValue();

			String _base = pair.substring(0, 3);
			String _quotee = pair.substring(3);

			if (_base.equals("USD")) {
				// ok, possible target ...
				if (_quotee.equals(base)) {
					return (1.0 / rate) / refQuote;
				} else if (_quotee.equals(quotee)) {
					return 1.0 / rate;
				}
			} else if (_quotee.equals("USD")) {
				// ok, possible target ...
				if (_base.equals(base)) {
					return rate / refQuote;
				} else if (_base.equals(quotee)) {
					return rate;
				}
			}
		}
		return ret;
	}

	@Override
	public TSContainer2 feesSeries() {
		return feeSeries;
	}

	public List<String> getRows() {
		return rows;
	}

}
