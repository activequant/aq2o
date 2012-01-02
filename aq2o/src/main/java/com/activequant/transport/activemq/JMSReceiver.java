package com.activequant.transport.activemq;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.activequant.transport.IReceiver;
import com.activequant.utils.MapToString;
import com.activequant.utils.events.Event;

class JMSReceiver implements IReceiver, MessageListener {
    JMSReceiver() {
    }

    private Logger log = Logger.getLogger(JMSReceiver.class.getName());
    private Event<Map<String, Object>> msgRecEvent = new Event<Map<String, Object>>();
    /**
     * the converter.
     */
    private MapToString mapToString = new MapToString();

    public Event<Map<String, Object>> getMsgRecEvent() {
        return msgRecEvent;
    }

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String text = textMessage.getText();
                // convert to hashmap.
                Map<String, Object> map = mapToString.convert(text);
                msgRecEvent.fire(map);
            } catch (JMSException e) {
                log.warn("Error while receiving message", e);
            }
        }
    }

}
