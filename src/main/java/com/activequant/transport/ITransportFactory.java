package com.activequant.transport;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.exceptions.TransportException;

/**
 * If possible, always use the typed methods. 
 * @author ustaudinger
 *
 */
public interface ITransportFactory {

    public IPublisher getPublisher(ETransportType transType, String id) throws TransportException;

    public IReceiver getReceiver(ETransportType transType, String id) throws TransportException;
	
    public IPublisher getPublisher(ETransportType transType, Instrument instrument) throws TransportException;

    public IReceiver getReceiver(ETransportType transType, Instrument instrument) throws TransportException;

    public IPublisher getPublisher(ETransportType transType, MarketDataInstrument instrument) throws TransportException;

    public IReceiver getReceiver(ETransportType transType, MarketDataInstrument instrument) throws TransportException;

    public IPublisher getPublisher(ETransportType transType, TradeableInstrument instrument) throws TransportException;

    public IReceiver getReceiver(ETransportType transType, TradeableInstrument instrument) throws TransportException;

    public IPublisher getPublisher(String channel) throws TransportException;

    public IReceiver getReceiver(String channel) throws TransportException;

}
