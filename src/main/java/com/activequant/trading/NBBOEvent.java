package com.activequant.trading;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.tools.streaming.TimeStreamEvent;


public class NBBOEvent extends TimeStreamEvent {
	
	private final QuoteEvent bid, ask; 
	private final String tradeableId; 
	
	public NBBOEvent(Date8Time6 ts, QuoteEvent bid, QuoteEvent ask) {
		super(ts);
		this.bid = bid; 
		this.ask = ask;
		if(bid!=null)tradeableId = bid.getTradeableInstrumentId();
		else if(ask!=null)tradeableId = ask.getTradeableInstrumentId();
		// TODO: throw invalid argument exception. 
		else tradeableId = null; 
	}

	public String getEventType(){return "NBBO";}
	
	public String getTradeableInstrumentId(){return tradeableId;}
	

}
