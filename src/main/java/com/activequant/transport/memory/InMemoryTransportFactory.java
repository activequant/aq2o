package com.activequant.transport.memory;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.exceptions.TransportException;
import com.activequant.transport.ETransportType;
import com.activequant.transport.IPublisher;
import com.activequant.transport.IReceiver;
import com.activequant.transport.ITransportFactory;

public class InMemoryTransportFactory implements ITransportFactory {

	@Override
	public IPublisher getPublisher(ETransportType transType,
			Instrument instrument) throws TransportException {
		return null;
	}

	@Override
	public IReceiver getReceiver(ETransportType transType, Instrument instrument)
			throws TransportException {
		return null;
	}

	@Override
	public IPublisher getPublisher(ETransportType transType,
			MarketDataInstrument instrument) throws TransportException {
		return null;
	}

	@Override
	public IReceiver getReceiver(ETransportType transType,
			MarketDataInstrument instrument) throws TransportException {
		return null;
	}

	@Override
	public IPublisher getPublisher(ETransportType transType,
			TradeableInstrument instrument) throws TransportException {
		return null;
	}

	@Override
	public IReceiver getReceiver(ETransportType transType,
			TradeableInstrument instrument) throws TransportException {
		return null;
	}

	@Override
	public IPublisher getPublisher(String channel) throws TransportException {
		return null;
	}

	@Override
	public IReceiver getReceiver(String channel) throws TransportException {
		return null;
	}

	
	
}
