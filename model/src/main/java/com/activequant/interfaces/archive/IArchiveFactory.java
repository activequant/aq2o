package com.activequant.interfaces.archive;

import com.activequant.domainmodel.TimeFrame;

/**
 * Reimplement for your own need. 
 * 
 * Use TimeFrame.RAW for non-time-discrete data.  
 * 
 * @author GhostRider
 *
 */
public interface IArchiveFactory {

	/**
	 * Used to fetch a reader for a specific timeframe. 
	 * 
	 * @param tf
	 * @return
	 */
    IArchiveReader getReader(TimeFrame tf);

    /**
     * Used to fetch a writer for a specific timeframe. 
     * 
     * @param tf
     * @return
     */
    IArchiveWriter getWriter(TimeFrame tf);
}
