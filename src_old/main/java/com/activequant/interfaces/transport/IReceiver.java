package com.activequant.interfaces.transport;

import java.util.Map;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.interfaces.utils.IEventSource;

public interface IReceiver {

    /**
     * use it to register for incoming data
     * 
     * @return
     */
	IEventSource<Map<String, Object>> getRawMsgRecEvent();
	IEventSource<PersistentEntity> getMsgRecEvent();
}
