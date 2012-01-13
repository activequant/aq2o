package com.activequant.trading;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.Tuple;
import com.activequant.tools.streaming.TimeStreamEvent;


public class NBBOEvent extends TimeStreamEvent {
	
	private final Tuple<Double, Double> bid, ask; 
	private final String tradeableId;

	public NBBOEvent(String tradInstId, Date8Time6 ts, Tuple<Double, Double> bid, Tuple<Double, Double> ask) {
		super(ts);
		this.bid = bid; 
		this.ask = ask;		
		this.tradeableId = tradInstId; 
	}

	public Tuple<Double, Double> getAsk() {
		return ask;
	} 
	
	public Tuple<Double, Double> getBid() {
		return bid;
	}

	public String getEventType(){return "NBBO";}
	
	public String getTradeableInstrumentId(){return tradeableId;}
	

}
