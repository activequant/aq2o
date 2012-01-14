package com.activequant.transport.memory;

import java.util.HashMap;
import java.util.Map;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.exceptions.TransportException;
import com.activequant.transport.ETransportType;
import com.activequant.transport.IPublisher;
import com.activequant.transport.IReceiver;
import com.activequant.transport.ITransportFactory;
import com.activequant.utils.events.Event;

public class InMemoryTransportFactory implements ITransportFactory {

	private Map<String, Event<Map<String, Object>>> eventMap = new HashMap<String, Event<Map<String, Object>>>();
	private Map<String, IPublisher> publisherMap = new HashMap<String, IPublisher>();
	private Map<String, IReceiver> recvMap = new HashMap<String, IReceiver>();
	
	private Event<Map<String, Object>> getEventInstance(String channelName){
		if(!eventMap.containsKey(channelName))eventMap.put(channelName, new Event<Map<String, Object>>());
		return eventMap.get(channelName);
	}
	
	private IPublisher getIPub(String channelName){
		if(!publisherMap.containsKey(channelName)){
			IPublisher p = new InMemoryPublisher(getEventInstance(channelName));
			publisherMap.put(channelName, p);
		}
		return publisherMap.get(channelName);
	}
	
	private IReceiver getIRecv(String channelName){
		if(!recvMap.containsKey(channelName)){
			IReceiver p = new InMemoryReceiver(getEventInstance(channelName));
			recvMap.put(channelName, p);
		}
		return recvMap.get(channelName);
	}	
	
	@Override
	public IPublisher getPublisher(ETransportType transType,
			Instrument instrument) throws TransportException {
		String channelName = transType.toString()+":"+instrument.toString();
		return getIPub(channelName);
	}

	@Override
	public IReceiver getReceiver(ETransportType transType, Instrument instrument)
			throws TransportException {
		String channelName = transType.toString()+":"+instrument.toString();
		return getIRecv(channelName);
	}

	@Override
	public IPublisher getPublisher(ETransportType transType,
			MarketDataInstrument instrument) throws TransportException {
		String channelName = transType.toString()+":"+instrument.toString();
		return getIPub(channelName);
	}

	@Override
	public IReceiver getReceiver(ETransportType transType,
			MarketDataInstrument instrument) throws TransportException {
		String channelName = transType.toString()+":"+instrument.toString();
		return getIRecv(channelName);
	}

	@Override
	public IPublisher getPublisher(ETransportType transType,
			TradeableInstrument instrument) throws TransportException {
		String channelName = transType.toString()+":"+instrument.toString();
		return getIPub(channelName);
	}

	@Override
	public IReceiver getReceiver(ETransportType transType,
			TradeableInstrument instrument) throws TransportException {
		String channelName = transType.toString()+":"+instrument.toString();
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
