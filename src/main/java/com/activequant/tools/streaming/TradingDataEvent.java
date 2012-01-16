package com.activequant.tools.streaming;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.transport.ETransportType;

public abstract class TradingDataEvent extends TimeStreamEvent {
	
	private final TradeableInstrument instr; 
	
	public ETransportType getEventType(){return ETransportType.TRAD_DATA;}
	
	public TradingDataEvent(TimeStamp ts, String className, TradeableInstrument instr)
	{
		super(ts, className);
		this.instr = instr; 
	}
		
	@Override
	public String getId() {
		return getTimeStamp().toString();
	}

	public TradeableInstrument getTradInst() {
		return instr;
	}

}
