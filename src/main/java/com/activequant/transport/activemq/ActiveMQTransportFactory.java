package com.activequant.transport.activemq;

import java.util.HashMap;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.transport.ETransportType;
import com.activequant.transport.IPublisher;
import com.activequant.transport.IReceiver;
import com.activequant.transport.ITransportFactory;

/**
 * 
 * @author ustaudinger
 * 
 */
public class ActiveMQTransportFactory implements ITransportFactory {

    private static ActiveMQConnectionFactory connectionFactory;
    private static Connection connection;
    private static Session session;
    private Logger log = Logger.getLogger(ActiveMQConnectionFactory.class.getName());

    private HashMap<String, IPublisher> publisherMap = new HashMap<String, IPublisher>();
    private HashMap<String, IReceiver> receiverMap = new HashMap<String, IReceiver>();

    public ActiveMQTransportFactory(String host, int port) throws Exception {
        // failover: means that it will automatically reconnect.
        String conUrl = "failover:tcp://" + host + ":" + port + "??wireFormat.maxInactivityDuration=0";
        log.info("Constructing ActiveMQTransportFactory for " + conUrl);

        connectionFactory = new ActiveMQConnectionFactory(conUrl);
        connectionFactory.setProducerWindowSize(1024000);
        connection = connectionFactory.createTopicConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public ActiveMQTransportFactory() throws Exception {
        String conUrl = "vm://localhost";
        log.info("Constructing embedded ActiveMQTransportFactory for " + conUrl);

        connectionFactory = new ActiveMQConnectionFactory(conUrl);
        connectionFactory.setProducerWindowSize(1024000);
        connection = connectionFactory.createTopicConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    }

    private void createPublisher(String destination) throws Exception {
        String originalDestination = destination;
        // reducing the amount of topics. working through selectors.
        if (destination.length() > 5)
            destination = destination.substring(0, 4); // +
                                                       // (destination.hashCode()
                                                       // % 5);
        if (!publisherMap.containsKey(originalDestination)) {
            Destination topic = session.createTopic(destination);
            MessageProducer producer = session.createProducer(topic);
            JMSPublisher j = new JMSPublisher(session, producer, originalDestination);
            publisherMap.put(originalDestination, j);
            if (log.isDebugEnabled())
                log.debug("Added publisher for " + originalDestination);
        }
    }

    private void createReceiver(String destination) throws Exception {
        String originalDestination = destination;
        // reducing the amount of topics. working through selectors.

        if (destination.length() > 5)
            destination = destination.substring(0, 4); // +
                                                       // (destination.hashCode()
                                                       // % 5);
        if (!receiverMap.containsKey(originalDestination)) {
            Destination topic = session.createTopic(destination);
            MessageConsumer consumer = session.createConsumer(topic, "channelId='" + originalDestination + "'");
            JMSReceiver j = new JMSReceiver();
            consumer.setMessageListener(j);
            receiverMap.put(originalDestination, j);
            if (log.isDebugEnabled())
                log.debug("Added receiver for " + originalDestination);
        }
    }

    public synchronized IPublisher getPublisher(ETransportType transType, Instrument instrument) throws Exception {
        String destination = transType.toString() + "." + instrument.getId();
        createPublisher(destination);
        return publisherMap.get(destination);
    }

    public synchronized IReceiver getReceiver(ETransportType transType, Instrument instrument) throws Exception {
        String destination = transType.toString() + "." + instrument.getId();
        createReceiver(destination);
        return receiverMap.get(destination);
    }

    public IPublisher getPublisher(ETransportType transType, MarketDataInstrument instrument) throws Exception {
        String destination = transType.toString() + "." + instrument.getMdProvider() + "."
                + instrument.getProviderSpecificId();
        createPublisher(destination);
        return publisherMap.get(destination);
    }

    public IReceiver getReceiver(ETransportType transType, MarketDataInstrument instrument) throws Exception {
        String destination = transType.toString() + "." + instrument.getMdProvider() + "."
                + instrument.getProviderSpecificId();
        createReceiver(destination);
        return receiverMap.get(destination);

    }

    public IPublisher getPublisher(String channel) throws Exception {
        createPublisher(channel);
        return publisherMap.get(channel);
    }

    public IReceiver getReceiver(String channel) throws Exception {
        createReceiver(channel);
        return receiverMap.get(channel);

    }

    public IPublisher getPublisher(ETransportType transType, TradeableInstrument instrument) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public IReceiver getReceiver(ETransportType transType, TradeableInstrument instrument) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}