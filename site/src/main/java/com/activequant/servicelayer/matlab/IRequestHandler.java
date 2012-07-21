package com.activequant.servicelayer.matlab;

import com.activequant.archive.TSContainer;
import com.activequant.domainmodel.TimeStamp;

public interface IRequestHandler {

	boolean handles(String field);
	
	TSContainer handle(String seriesId, String fieldName, TimeStamp start, TimeStamp end);
	
}
