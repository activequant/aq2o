package com.activequant.dao;

import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TradeableInstrument;

public interface ITradeableInstrumentDao extends IEntityDao<TradeableInstrument> {
  
	TradeableInstrument findByProvId(String providerId, String provSpecInstId);

}
