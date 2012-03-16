package com.activequant.tools.streaming;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.transport.ETransportType;

public class OrderStreamEvent extends TradingDataEvent {
	
	private OrderEvent oe; 

	public OrderStreamEvent(String tradeableId, TimeStamp ts, OrderEvent oe) {
		super(ts,OrderStreamEvent.class.getCanonicalName(), tradeableId);
		this.oe = oe; 
	}
	
	public ETransportType getEventType(){return ETransportType.TRAD_DATA;}

	public OrderEvent getOe() {
		return oe;
	}

	public void setOe(OrderEvent oe) {
		this.oe = oe;
	}

}
