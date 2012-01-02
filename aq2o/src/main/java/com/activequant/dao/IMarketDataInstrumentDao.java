package com.activequant.dao;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;

public interface IMarketDataInstrumentDao extends IEntityDao<MarketDataInstrument> {

    /**
     * fetch all mdinstruments for a specific provider.
     * 
     * @param providerId
     *            f.e. Bloomberg
     * 
     * @return
     */
    MarketDataInstrument[] findForProvider(String providerId);

    int countForProvider(String providerId);

    MarketDataInstrument[] findFor(Instrument instrument);

    MarketDataInstrument findByProvId(String providerId, String provSpecInstId);

    MarketDataInstrument[] findLike(String providerId, String provSpecInstId);

    MarketDataInstrument[] findForProvider(String providerId, int startIndex, int maxAmount);
}
