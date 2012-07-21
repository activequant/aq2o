package com.activequant.dao.inmemory;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.interfaces.dao.IMarketDataInstrumentDao;

public class InMemMDIDao extends InMemoryDao<MarketDataInstrument> implements IMarketDataInstrumentDao {

	@Override
	public MarketDataInstrument load(String primaryKey) throws DaoException {
		MarketDataInstrument mdi = new MarketDataInstrument();
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
	public MarketDataInstrument[] findForProvider(String providerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countForProvider(String providerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getProviders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketDataInstrument[] findFor(Instrument instrument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketDataInstrument findFor(String providerId, Instrument instrument) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketDataInstrument findByProvId(String providerId, String provSpecInstId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketDataInstrument[] findLike(String providerId, String provSpecInstId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarketDataInstrument[] findForProvider(String providerId, int startIndex, int maxAmount) {
		// TODO Auto-generated method stub
		return null;
	}



}
