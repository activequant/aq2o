package com.activequant.transport.activemq;

import java.util.Map;

import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.transport.IPublisher;
import com.activequant.utils.MapToString;

class JMSPublisher implements IPublisher {

    private MessageProducer producer;
    private Session session;
    private MapToString mapToString = new MapToString();
    private Logger log = Logger.getLogger(JMSPublisher.class);
    private final String channelId;

    JMSPublisher(Session session, MessageProducer producer, String channelId) {
        this.producer = producer;
        this.session = session;
        this.channelId = channelId;
    }

    public void send(Map<String, Object> message) throws Exception {
        String text = mapToString.convert(message);
        if (log.isDebugEnabled())
            log.debug("[channelId=" + channelId + "] Message: " + text);
        TextMessage tm = session.createTextMessage(text);
        tm.setStringProperty("channelId", channelId);
        producer.send(tm);
    }

	@Override
	public void send(PersistentEntity entity) throws Exception {
		send(entity.propertyMap());
	}

}
