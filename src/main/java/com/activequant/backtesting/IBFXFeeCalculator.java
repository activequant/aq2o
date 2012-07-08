package com.activequant.backtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.timeseries.TSContainer2;
import com.activequant.timeseries.TypedColumn;

/**
 * http://www.interactivebrokers.com/de/accounts/fees/commission.php?ib_entity=de
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
	
	public void updateRefRate(String id, Double ref){
		conversionSheet.put(id, ref);
	}
	
	@Override
	public void track(OrderEvent orderEvent) {
		if (orderEvent instanceof OrderFillEvent) {
			// 
			OrderFillEvent ofe = (OrderFillEvent) orderEvent;
			// 
			String tid = ofe.getOptionalInstId();
			
			
			double volume = ofe.getFillAmount();
			double execPrice = ofe.getFillPrice();
			// 
			double tradedValueInQuotee = volume * execPrice; 
			String base = tid.substring(0,3);
			String quotee = tid.substring(3);
			
			double conversionRate = 1.0;
			if(base.equals("USD")){
				conversionRate = 1.0/execPrice; 
			}
			else if(quotee.equals("USD")){
				conversionRate = 1.0; 
			}
			else{
				conversionRate = getConversionRate(base, quotee, execPrice);
			}
			 
			double tradedValueInUsd = conversionRate * tradedValueInQuotee; 
			double commission = Math.max((0.2 * tickSizeAcctCurrency * tradedValueInUsd), 2.50);
			// track it. 
			feeSeries.setValue(tid, ofe.getCreationTimeStamp(), commission);			
		}
	}

	
	/**
	 * iterates over the quote sheets to find the first matching pair to convert baed on base or quotee to USD. 
	 * 
	 * @param base
	 * @param quotee
	 */
	private double getConversionRate(String base, String quotee, Double refQuote){
		//
		double ret = 1.0; 
		Iterator<Entry<String, Double>> it = conversionSheet.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Double> entry = it.next();
			String pair = entry.getKey();
			Double rate = entry.getValue();
					
			String _base = pair.substring(0,3);
			String _quotee = pair.substring(3);
			
			if(_base.equals("USD")){
				// ok, possible target ... 				
				if(_quotee.equals(base)){
					return (1.0/rate)/ refQuote; 
				}
				else if(_quotee.equals(quotee)){
					return 1.0/rate; 
				}
			}		
			else if(_quotee.equals("USD")){
				// ok, possible target ... 				
				if(_base.equals(base)){
					return rate / refQuote; 
				}
				else if(_base.equals(quotee)){
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

}
