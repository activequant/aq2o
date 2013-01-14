package com.activequant.trading.systems.sma;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.exceptions.IncompleteOrderInstructions;
import com.activequant.domainmodel.exceptions.UnsupportedOrderType;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.trade.order.MarketOrder;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.interfaces.trading.IOrderTracker;
import com.activequant.trading.AbstractTSBase;
import com.activequant.trading.datamodel.InstrumentTable;
import com.activequant.utils.RenjinCore;

/**
 * Simple Moving Average class that processes a stream event. 
 * 
 * @author GhostRider
 *
 */
public class SimpleMovingAverage extends AbstractTSBase {
	
	private List<Double> midpoints = new ArrayList<Double>();
 	private RenjinCore R = new RenjinCore();
	private double currentPos = 0;
	private DecimalFormat dcf = new DecimalFormat("#.00");
	private MarketDataInstrument mdi; 
	private TradeableInstrument  tdi; 
	protected boolean isRunning = false; 
	
	@Override
	public void start() throws Exception {		
		// 
		mdi = new MarketDataInstrument("CSV", "SOY");		
		tdi = new TradeableInstrument("CSV", "SOY");
		// add to local memory environment. 
		addInstrument(mdi, tdi);			
		getInstrumentTable().setValueAt(0.01, getInstrumentTable().getRowIndexOf(mdi.getId()), InstrumentTable.Columns.TICKSIZE.colIdx());
		getInstrumentTable().setValueAt(12.50, getInstrumentTable().getRowIndexOf(mdi.getId()), InstrumentTable.Columns.TICKVALUE.colIdx());
		getInstrumentTable().signalUpdate();
		isRunning = true; 
	}

	@Override
	public void stop() throws Exception {
		isRunning = false; 		
	}
	
	public boolean isRunning(){
		return isRunning; 
	}
	
	/**
	 * Called if there is a new market stream data event.
	 * 
	 */
	@Override
	public void process(StreamEvent se) {
		super.process(se);
		TimeStamp ts = se.getTimeStamp();
		
		if(se.getEventType().equals(ETransportType.MARKET_DATA) && se instanceof MarketDataSnapshot){
			Double mid = ((MarketDataSnapshot)se).getBidPrices()[0] +  ((MarketDataSnapshot)se).getAskPrices()[0];
			mid /= 2.0; 
			midpoints.add(mid);
			// restricting total length of our stored data ... 
			if(midpoints.size()>100)
				midpoints.remove(0);
			// size calculation
			if(midpoints.size()==100){
				// calculate some stuff.
				try {
					R.put("x", midpoints.toArray(new Double[]{}));
					R.execute("summedValue = sum(x)");
					Double sma = R.getDoubleVector("summedValue").getElementAsObject(0)/midpoints.size();
					
					double tgtPos = Math.signum(mid - sma.doubleValue());
					System.out.println(ts.getDate()+ " \tClose: " + dcf.format(mid) + " \tMean: " + dcf.format(sma) + "\tPos: " + dcf.format(tgtPos));
					
					// creating orders ... 
					if(tgtPos != currentPos){
						// 
						double posDifference = Math.abs(currentPos - tgtPos); 
						currentPos = tgtPos;
						// 
						MarketOrder mo = new MarketOrder();
						mo.setTradInstId(tdi.getId());
						mo.setQuantity(posDifference);
						mo.setOrderSide(tgtPos>0?OrderSide.BUY:OrderSide.SELL);
						IOrderTracker ot = this.env.getExchange().prepareOrder(mo);
						ot.submit();
					}					
				} catch (ScriptException e) {
					e.printStackTrace();
				} catch (UnsupportedOrderType e) {
					e.printStackTrace();
				} catch (IncompleteOrderInstructions e) {
					e.printStackTrace();
				}				
			}
		}
	}	
}
