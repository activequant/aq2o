package com.activequant.transport;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TradeableInstrument;

public interface ITransportFactory {

    public IPublisher getPublisher(ETransportType transType, Instrument instrument) throws Exception;

    public IReceiver getReceiver(ETransportType transType, Instrument instrument) throws Exception;

    public IPublisher getPublisher(ETransportType transType, MarketDataInstrument instrument) throws Exception;

    public IReceiver getReceiver(ETransportType transType, MarketDataInstrument instrument) throws Exception;

    public IPublisher getPublisher(ETransportType transType, TradeableInstrument instrument) throws Exception;

    public IReceiver getReceiver(ETransportType transType, TradeableInstrument instrument) throws Exception;

    public IPublisher getPublisher(String channel) throws Exception;

    public IReceiver getReceiver(String channel) throws Exception;

}
