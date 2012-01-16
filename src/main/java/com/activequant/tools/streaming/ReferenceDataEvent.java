package com.activequant.tools.streaming;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.transport.ETransportType;

public abstract class ReferenceDataEvent extends TimeStreamEvent {
	
	private final Instrument instr; 
	
	public ETransportType getEventType(){return ETransportType.REF_DATA;}
	
	public ReferenceDataEvent(TimeStamp ts, String className, Instrument instr)
	{
		super(ts, className);
		this.instr = instr; 
	}
		
	@Override
	public String getId() {
		return getTimeStamp().toString();
	}

	public Instrument getInstrument() {
		return instr;
	}

}
