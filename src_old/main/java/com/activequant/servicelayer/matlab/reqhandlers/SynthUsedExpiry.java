package com.activequant.servicelayer.matlab.reqhandlers;

import com.activequant.archive.TSContainer;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.servicelayer.matlab.IRequestHandler;

public class SynthUsedExpiry implements IRequestHandler {

	private IDaoFactory daof; 
	private IArchiveFactory archf; 
	
	public SynthUsedExpiry(IDaoFactory daof, IArchiveFactory archf){
		this.daof = daof; 
		this.archf = archf; 
	}
	
	@Override
	public boolean handles(String field) {
		if(field.equals("synth_used_expiry".toUpperCase()))
			return true; 
		return false;
	}

	@Override
	public TSContainer handle(String seriesId, String fieldName, TimeStamp start, TimeStamp end) {
		return null;
	}

}
