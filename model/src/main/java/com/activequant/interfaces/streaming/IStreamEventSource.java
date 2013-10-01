package com.activequant.interfaces.streaming;

import com.activequant.domainmodel.ETransportType;

public interface IStreamEventSource {
    void subscribe(IStreamEventSink sink, ETransportType eventType);
    void unsubscribe(IStreamEventSink sink, ETransportType eventType);
    void start();

}
