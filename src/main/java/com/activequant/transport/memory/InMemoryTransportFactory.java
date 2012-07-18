package com.activequant.transport.memory;

import java.util.HashMap;
import java.util.Map;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.utils.events.Event;

public class InMemoryTransportFactory implements ITransportFactory {

	private Map<String, Event<Map<String, Object>>> rawEventMap = new HashMap<String, Event<Map<String, Object>>>();
	private Map<String, Event<PersistentEntity>> eventMap = new HashMap<String, Event<PersistentEntity>>();
	private Map<String, IPublisher> publisherMap = new HashMap<String, IPublisher>();
	private Map<String, IReceiver> recvMap = new HashMap<String, IReceiver>();
	
	private Event<Map<String, Object>> getRawEventInstance(String channelName){
		if(!rawEventMap.containsKey(channelName))rawEventMap.put(channelName, new Event<Map<String, Object>>());
		return rawEventMap.get(channelName);
	}
	
	private Event<PersistentEntity> getEventInstance(String channelName){
		if(!eventMap.containsKey(channelName))eventMap.put(channelName, new Event<PersistentEntity>());
		return eventMap.get(channelName);
	}
	
	
	private IPublisher getIPub(String channelName){
		if(!publisherMap.containsKey(channelName)){
			IPublisher p = new InMemoryPublisher(getRawEventInstance(channelName), getEventInstance(channelName));
			publisherMap.put(channelName, p);
		}
		return publisherMap.get(channelName);
	}
	
	private IReceiver getIRecv(String channelName){
		if(!recvMap.containsKey(channelName)){
			IReceiver p = new InMemoryReceiver(getRawEventInstance(channelName), getEventInstance(channelName));
			recvMap.put(channelName, p);
		}
		return recvMap.get(channelName);
	}	
	
	
	@Override
	public IPublisher getPublisher(ETransportType transType,
			String id) throws TransportException {
		String channelName = transType.toString()+":"+id;
		return getIPub(channelName);
	}

	@Override
	public IReceiver getReceiver(ETransportType transType, String id)
			throws TransportException {
		String channelName = transType.toString()+":"+id;
		return getIRecv(channelName);
	}
	
	@Override
	public IPublisher getPublisher(ETransportType transType,
			Instrument instrument) throws TransportException {
		String channelName = transType.toString()+":"+instrument.getId();
		return getIPub(channelName);
	}

	@Override
	public IReceiver getReceiver(ETransportType transType, Instrument instrument)
			throws TransportException {
		String channelName = transType.toString()+":"+instrument.getId();
		return getIRecv(channelName);
	}

	@Override
	public IPublisher getPublisher(ETransportType transType,
			MarketDataInstrument instrument) throws TransportException {
		String channelName = transType.toString()+":"+instrument.getId();
		return getIPub(channelName);
	}

	@Override
	public IReceiver getReceiver(ETransportType transType,
			MarketDataInstrument instrument) throws TransportException {
		String channelName = transType.toString()+":"+instrument.getId();
		return getIRecv(channelName);
	}

	@Override
	public IPublisher getPublisher(ETransportType transType,
			TradeableInstrument instrument) throws TransportException {
		String channelName = transType.toString()+":"+instrument.getId();
		return getIPub(channelName);
	}

	@Override
	public IReceiver getReceiver(ETransportType transType,
			TradeableInstrument instrument) throws TransportException {
		String channelName = transType.toString()+":"+instrument.getId();
		return getIRecv(channelName);
	}

	@Override
	public IPublisher getPublisher(String channel) throws TransportException {
		return getIPub(channel);
	}

	@Override
	public IReceiver getReceiver(String channel) throws TransportException {
		return getIRecv(channel);
	}

	
	
}
