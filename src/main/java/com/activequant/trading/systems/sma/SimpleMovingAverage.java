package com.activequant.trading.systems.sma;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.tools.streaming.MarketDataSnapshot;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.trading.AbstractTSBase;
import com.activequant.transport.ETransportType;
import com.activequant.utils.RenjinCore;

/**
 * 
 * @author ustaudinger
 *
 */
public class SimpleMovingAverage extends AbstractTSBase {
	
	private List<Double> closes = new ArrayList<Double>();
	private RenjinCore R = new RenjinCore();
	private int currentPos = 0; 
	
	@Override
	public void start() throws Exception {		
		// 
		MarketDataInstrument mdi = new MarketDataInstrument("CSV", "SOY");		
		TradeableInstrument tdi = new TradeableInstrument("CSV", "SOY");
		// add to local memory environment. 
		addInstrument(mdi, tdi);			
	}

	@Override
	public void stop() throws Exception {
	}
	
	/**
	 * Called if there is a new market stream data event.
	 * 
	 */
	@Override
	public void process(StreamEvent se) {
		super.process(se);
		TimeStamp ts = se.getTimeStamp();
		// System.out.println(ts.getDate()+ " - " + se.getEventType());
		// 
		if(se.getEventType().equals(ETransportType.MARKET_DATA)){
			Double mid = ((MarketDataSnapshot)se).getBidPrices()[0] +  ((MarketDataSnapshot)se).getAskPrices()[0];
			mid /= 2.0; 
			closes.add(mid);
			// restricting total length of our stored data ... 
			if(closes.size()>100)
				closes.remove(0);
			
			
			// size calculation
			if(closes.size()>50){
				// calculate some stuff.
				try {
					R.put("x", closes.toArray(new Double[]{}));
					R.execute("sma = mean(x)");
					Double sma = R.getDoubleVector("sma").getElementAsObject(0);
					double tgtPos = Math.signum(mid - sma.doubleValue());
					System.out.println("Calculated mean : " + sma + " \t\t--> " + tgtPos);
				} catch (ScriptException e) {
					e.printStackTrace();
				}				
			}
		}
	}	
}
