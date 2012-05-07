package com.activequant.dao.inmemory;

import com.activequant.dao.IAdjustedSeriesDateEntryDao;
import com.activequant.dao.ICountryDao;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.dao.IPortfolioDao;
import com.activequant.dao.IPositionDao;
import com.activequant.dao.IRegionDao;
import com.activequant.dao.ISecurityChainDao;
import com.activequant.dao.ITradeableInstrumentDao;
import com.activequant.dao.IVenueDao;
import com.activequant.domainmodel.Instrument;

public class InMemoryDaoFactory implements IDaoFactory {

	@Override
	public ICountryDao countryDao() {
		return null;
	}

	@Override
	public IRegionDao regionDao() {
		return null;
	}

	@Override
	public IInstrumentDao instrumentDao() {
		return new InMemoryInstrumentDao();
	}

	@Override
	public IVenueDao venueDao() {
		return null;
	}

	@Override
	public IMarketDataInstrumentDao mdiDao() {
		return new InMemMDIDao();
	}

	@Override
	public ITradeableInstrumentDao tradeableDao() {
		return new InMemTDIDao();
	}

	@Override
	public IPositionDao positionDao() {
		return null;
	}

	@Override
	public IPortfolioDao portfolioDao() {
		return null;
	}

	@Override
	public ISecurityChainDao securityChainDao() {
		return null;
	}

	@Override
	public IAdjustedSeriesDateEntryDao adjSerDtEntryDao() {
		return null;
	}

}
