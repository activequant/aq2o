package com.activequant.backtesting;

import java.util.ArrayList;

import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.timeseries.TSContainer2;
import com.activequant.timeseries.TypedColumn;

/**
 * http://www.interactivebrokers.com/de/accounts/fees/commission.php?ib_entity=de
 * 
 * Makes a flat commission structure - (no three tier structure). 
 * 
 * @author GhostRider
 *
 */
public class IBFeeCalculator implements IFeeCalculator {

	private String accountBaseCurrency = "USD";
	private double minimumPerOrder = 2.5; 
	private double commissionBps = 0.2; //
	private double tickSizeAcctCurrency = 0.0001;
	private TSContainer2 feeSeries = new TSContainer2("FEES", new ArrayList<String>(), new ArrayList<TypedColumn>());
	
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
			double tradedValue = volume * execPrice; 
			// convert traded value to USD. 
			// 
			double conversionRate = 1.0; 
			double tradedValueInUsd = conversionRate * tradedValue; 
			double commission = Math.max((0.2 * tickSizeAcctCurrency * tradedValueInUsd), 2.50);
			// track it. 
			feeSeries.setValue(tid, ofe.getCreationTimeStamp(), commission);
			
		}
	}

	@Override
	public TSContainer2 feesSeries() {
		return feeSeries;
	}

}
