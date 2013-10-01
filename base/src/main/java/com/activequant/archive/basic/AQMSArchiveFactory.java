package com.activequant.archive.basic;

import com.activequant.domainmodel.TimeFrame;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveReader;
import com.activequant.interfaces.archive.IArchiveWriter;

/**
 * 
 * Intentionally partially blank implementation of an Archive Factory 
 * for interfacing with an ActiveQuant Master Server. 
 * 
 * @author GhostRider
 *
 */
public class AQMSArchiveFactory implements IArchiveFactory {

	private String baseUrl;
	public AQMSArchiveFactory(String baseUrl){
		this.baseUrl = baseUrl; 
	}
	
	@Override
	public IArchiveReader getReader(TimeFrame tf) {
		return new AQMSArchiveReader(baseUrl, tf);
	}

	@Override
	public IArchiveWriter getWriter(TimeFrame tf) {
		return null;
	}

}
