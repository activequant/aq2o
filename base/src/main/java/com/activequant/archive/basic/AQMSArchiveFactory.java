package com.activequant.archive.basic;

import com.activequant.domainmodel.TimeFrame;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveReader;
import com.activequant.interfaces.archive.IArchiveWriter;

/**
 * 
 * @author GhostRider
 *
 */
public class AQMSArchiveFactory implements IArchiveFactory {

	@Override
	public IArchiveReader getReader(TimeFrame tf) {
		return null;
	}

	@Override
	public IArchiveWriter getWriter(TimeFrame tf) {
		return null;
	}

}
