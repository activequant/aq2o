package com.activequant.transport.activemq;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.transport.IReceiver;
import com.activequant.utils.MapToString;
import com.activequant.utils.events.Event;
import com.activequant.utils.events.IEventSource;

class JMSReceiver implements IReceiver, MessageListener {
    JMSReceiver() {
    }

    private Logger log = Logger.getLogger(JMSReceiver.class.getName());
    private Event<Map<String, Object>> rawMsgRecEvent = new Event<Map<String, Object>>();
    private Event<PersistentEntity> msgRecEvent = new Event<PersistentEntity>();

    /**
     * 
     * the converter.
     */
    private MapToString mapToString = new MapToString();

    public IEventSource<Map<String, Object>> getRawMsgRecEvent() {
        return rawMsgRecEvent;
    }
    

    public IEventSource<PersistentEntity> getMsgRecEvent() {
        return msgRecEvent;
    }


    @SuppressWarnings("unchecked")
	public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String text = textMessage.getText();
                // convert to hashmap.
                Map<String, Object> map = mapToString.convert(text);
                rawMsgRecEvent.fire(map);
                // check if it is a persistent entity. 
                
                // TODO for speed up reasons, make it possible to disable this. 
                
                if(map.containsKey("CLASSNAME")){
                	Class<PersistentEntity> clazz;
					try {
						clazz = (Class<PersistentEntity>) 
								Class.forName((String)map.get("CLASSNAME"));
						PersistentEntity pe = clazz.newInstance();
						pe.initFromMap(map);
						msgRecEvent.fire(pe);
					} catch (ClassNotFoundException e) {
						log.warn("Map containing classname, but could not instantiate it. ", e);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}                	
                	 
                }
            } catch (JMSException e) {
                log.warn("Error while receiving message", e);
            }
        }
    }

}
