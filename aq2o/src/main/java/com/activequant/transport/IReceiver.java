package com.activequant.transport;

import java.util.Map;

import com.activequant.utils.events.Event;

public interface IReceiver {

    /**
     * use it to register for incoming data
     * 
     * @return
     */
    Event<Map<String, Object>> getMsgRecEvent();
}
