package com.activequant.transport.activemq;

import java.util.HashMap;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;

/**
 * 
 * @author ustaudinger
 * 
 */
public class ActiveMQTransportFactory implements ITransportFactory {

	private static ActiveMQConnectionFactory connectionFactory;
	private static Connection connection;
	private static Session session;
	private Logger log = Logger.getLogger(ActiveMQConnectionFactory.class
			.getName());

	private HashMap<String, IPublisher> publisherMap = new HashMap<String, IPublisher>();
	private HashMap<String, IReceiver> receiverMap = new HashMap<String, IReceiver>();

	/**
	 * constructs an activemq transport factory that connects to a specific host
	 * and port.
	 * 
	 * @param host
	 *            JMS server host
	 * @param port
	 *            JMS server port.
	 * @throws Exception
	 */
	public ActiveMQTransportFactory(String host, int port) throws Exception {
		// failover: means that it will automatically reconnect.
		String conUrl = "failover:tcp://" + host + ":" + port
				+ "??wireFormat.maxInactivityDuration=0";
		log.info("Constructing ActiveMQTransportFactory for " + conUrl);

		connectionFactory = new ActiveMQConnectionFactory(conUrl);
		connectionFactory.setProducerWindowSize(1024000);
		connectionFactory.setUseAsyncSend(true);
		connectionFactory.setOptimizeAcknowledge(true);
		connection = connectionFactory.createTopicConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	
	/**
	 * constructs an activemq transport factory that connects to a specific host
	 * and port.
	 * 
	 * @param host
	 *            JMS server host
	 * @param port
	 *            JMS server port.
	 * @throws Exception
	 */
	public ActiveMQTransportFactory(String host, int port, String username, String password) throws Exception {
		// failover: means that it will automatically reconnect.
		String conUrl = "failover:tcp://" + host + ":" + port
				+ "??wireFormat.maxInactivityDuration=0";
		log.info("Constructing ActiveMQTransportFactory for " + conUrl);
		// 
		connectionFactory = new ActiveMQConnectionFactory(conUrl);
		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);		
		connectionFactory.setProducerWindowSize(1024000);
		connectionFactory.setUseAsyncSend(true);		
		connectionFactory.setOptimizeAcknowledge(true);
		connection = connectionFactory.createTopicConnection(username, password);
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	
	/**
	 * constructs and in-memory active mq transport factory.
	 * 
	 * @throws Exception
	 */
	public ActiveMQTransportFactory() throws Exception {
		String conUrl = "vm://localhost";
		log.info("Constructing embedded ActiveMQTransportFactory for " + conUrl);

		connectionFactory = new ActiveMQConnectionFactory(conUrl);
		connectionFactory.setProducerWindowSize(1024000);
		connection = connectionFactory.createTopicConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	}

	/**
	 * creates a publisher.
	 * 
	 * @param destination
	 * @throws Exception
	 */
	private void createPublisher(String destination) throws TransportException {
		String originalDestination = destination;
		// reducing the amount of topics. working through selectors.
		if (destination.length() >= 5)
			destination = destination.substring(0, 4); // +
														// (destination.hashCode()
														// % 5);
		if (!publisherMap.containsKey(originalDestination)) {
			try {
				Destination topic = session.createTopic(destination);
				MessageProducer producer = session.createProducer(topic);
				JMSPublisher j = new JMSPublisher(session, producer,
						originalDestination);
				publisherMap.put(originalDestination, j);
				if (log.isDebugEnabled())
					log.debug("Added publisher for " + originalDestination);
			} catch (Exception ex) {
				throw new TransportException(ex);
			}
		}
	}

	private void createReceiver(String destination) throws TransportException {
		String originalDestination = destination;
		// reducing the amount of topics. working through selectors.
		if (destination.length() >= 5)
			destination = destination.substring(0, 4); 
		if (!receiverMap.containsKey(originalDestination)) {
			try {
				Destination topic = session.createTopic(destination);
				MessageConsumer consumer = null;
				if (originalDestination.endsWith(".*")) {
					consumer = session.createConsumer(topic);
				} else {
					consumer = session.createConsumer(topic, "channelId='"
							+ originalDestination + "'");
				}
				JMSReceiver j = new JMSReceiver();
				consumer.setMessageListener(j);
				receiverMap.put(originalDestination, j);
				if (log.isDebugEnabled())
					log.debug("Added receiver for " + originalDestination);
			} catch (Exception ex) {
				throw new TransportException(ex);
			}
		}
	}

	public synchronized IPublisher getPublisher(ETransportType transType,
			String id) throws TransportException {
		String destination = transType.toString() + "." + id;
		createPublisher(destination);
		return publisherMap.get(destination);
	}

	public synchronized IReceiver getReceiver(ETransportType transType,
			String id) throws TransportException {
		String destination = transType.toString() + "." + id;
		createReceiver(destination);
		return receiverMap.get(destination);
	}

	public synchronized IPublisher getPublisher(ETransportType transType,
			Instrument instrument) throws TransportException {
		String destination = transType.toString() + "." + instrument.getId();
		createPublisher(destination);
		return publisherMap.get(destination);
	}

	public synchronized IReceiver getReceiver(ETransportType transType,
			Instrument instrument) throws TransportException {
		String destination = transType.toString() + "." + instrument.getId();
		createReceiver(destination);
		return receiverMap.get(destination);
	}

	public IPublisher getPublisher(ETransportType transType,
			MarketDataInstrument instrument) throws TransportException {
		String destination = transType.toString() + "."
				+ instrument.getMdProvider() + "."
				+ instrument.getProviderSpecificId();
		createPublisher(destination);
		return publisherMap.get(destination);
	}

	public IReceiver getReceiver(ETransportType transType,
			MarketDataInstrument instrument) throws TransportException {
		String destination = transType.toString() + "."
				+ instrument.getMdProvider() + "."
				+ instrument.getProviderSpecificId();
		createReceiver(destination);
		return receiverMap.get(destination);

	}

	public IPublisher getPublisher(String channel) throws TransportException {
		createPublisher(channel);
		return publisherMap.get(channel);
	}

	public IReceiver getReceiver(String channel) throws TransportException {
		createReceiver(channel);
		return receiverMap.get(channel);

	}

	public IPublisher getPublisher(ETransportType transType,
			TradeableInstrument instrument) throws TransportException {
		String destination = transType.toString() + "."
				+ instrument.getTradingProvider() + "."
				+ instrument.getProviderSpecificId();
		createPublisher(destination);
		return publisherMap.get(destination);
	}

	public IReceiver getReceiver(ETransportType transType,
			TradeableInstrument instrument) throws TransportException {
		String destination = transType.toString() + "."
				+ instrument.getTradingProvider() + "."
				+ instrument.getProviderSpecificId();
		createReceiver(destination);
		return receiverMap.get(destination);
	}

}
