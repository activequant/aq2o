package com.activequant.transport;

import java.util.Map;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.utils.events.IEventSource;

public interface IReceiver {

    /**
     * use it to register for incoming data
     * 
     * @return
     */
	IEventSource<Map<String, Object>> getRawMsgRecEvent();
	IEventSource<PersistentEntity> getMsgRecEvent();
}
