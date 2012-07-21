package com.activequant.dao.inmemory;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.interfaces.dao.ITradeableInstrumentDao;

public class InMemTDIDao extends InMemoryDao<TradeableInstrument> implements ITradeableInstrumentDao{

	
	@Override
	public TradeableInstrument load(String primaryKey) throws DaoException {
		TradeableInstrument mdi = new TradeableInstrument();
		mdi.setProviderSpecificId(primaryKey);
		return mdi;
	}
	
	public String[] getNonExpiredInstruments(long referenceDate8) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] searchById(String searchString, int resultAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TradeableInstrument findByProvId(String providerId, String provSpecInstId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TradeableInstrument findFor(String providerId, Instrument instrument) {
		// TODO Auto-generated method stub
		return null;
	}




}
