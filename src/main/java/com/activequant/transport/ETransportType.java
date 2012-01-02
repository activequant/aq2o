package com.activequant.transport;

/**
 * Holds an enumeration of possible transport types. The framework provides the
 * adequate transport on request through the ITransportFactory implementation.
 * 
 * @author ustaudinger
 * 
 */
public enum ETransportType {

    /**
     * transport can be of type reference data or market data.
     */
    REF_DATA, MARKET_DATA;
}
