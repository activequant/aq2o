package com.activequant.domainmodel.streaming;

import com.activequant.domainmodel.TimeStamp;

/**
 * holds account data related events, such as CASH, USED_MARGIN, etc. 
 * 
 * 
 * @author GhostRider
 *
 */
public class AccountDataEvent extends TimeStreamEvent {

	private final String variableId; 
	public String getVariableId() {
		return variableId;
	}

	public Object getValue() {
		return value;
	}

	private final Object value; 
	
	public AccountDataEvent(TimeStamp ts, String variableId, Object value) {
		super(ts, AccountDataEvent.class.getCanonicalName());
		this.variableId = variableId; 
		this.value = value; 
	}

}
