package com.activequant.interfaces.streaming;

import com.activequant.domainmodel.streaming.StreamEvent;

public interface IStreamEventSink {
    void process(StreamEvent se);

}
