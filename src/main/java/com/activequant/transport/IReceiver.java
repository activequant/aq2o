package com.activequant.transport;

import java.util.Map;

import com.activequant.utils.events.IEventSource;

public interface IReceiver {

    /**
     * use it to register for incoming data
     * 
     * @return
     */
	IEventSource<Map<String, Object>> getMsgRecEvent();
}
