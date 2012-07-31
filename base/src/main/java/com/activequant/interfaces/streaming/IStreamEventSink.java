package com.activequant.interfaces.streaming;

import com.activequant.domainmodel.streaming.StreamEvent;

/**
 * 
 * @author GhostRider
 *
 */
public interface IStreamEventSink {
    void process(StreamEvent se);

}
