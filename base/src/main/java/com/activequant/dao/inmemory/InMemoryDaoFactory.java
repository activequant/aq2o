package com.activequant.dao.inmemory;

import com.activequant.interfaces.dao.IAccountDao;
import com.activequant.interfaces.dao.IAdjustedSeriesDateEntryDao;
import com.activequant.interfaces.dao.IBasicMacroEventDao;
import com.activequant.interfaces.dao.IClearedTradeDao;
import com.activequant.interfaces.dao.IClearerAccountSnapDao;
import com.activequant.interfaces.dao.ICountryDao;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.IGenericEntityDao;
import com.activequant.interfaces.dao.IInstrumentDao;
import com.activequant.interfaces.dao.IMarketDataInstrumentDao;
import com.activequant.interfaces.dao.IOrderEventDao;
import com.activequant.interfaces.dao.IOrderFillDao;
import com.activequant.interfaces.dao.IPNLDao;
import com.activequant.interfaces.dao.IPandSDao;
import com.activequant.interfaces.dao.IPerformanceReportDao;
import com.activequant.interfaces.dao.IPortfolioDao;
import com.activequant.interfaces.dao.IPortfolioSnapDao;
import com.activequant.interfaces.dao.IPositionDao;
import com.activequant.interfaces.dao.IRegionDao;
import com.activequant.interfaces.dao.IReportDao;
import com.activequant.interfaces.dao.ISecurityChainDao;
import com.activequant.interfaces.dao.ISubClearerAccountDao;
import com.activequant.interfaces.dao.ISubClearerAccountSnapDao;
import com.activequant.interfaces.dao.ITradeableInstrumentDao;
import com.activequant.interfaces.dao.IVenueDao;
/**
 * Can be used for in memory daos. 
 * Hugely underdeveloped. 
 * 
 * @author GhostRider
 *
 */
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

	@Override
	public IPerformanceReportDao perfDao() {
		return null;
	}

	@Override
	public IReportDao reportDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOrderEventDao orderEventDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOrderFillDao orderFillDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPandSDao pAndSDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccountDao accountDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IClearedTradeDao clearedTradeDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IClearerAccountSnapDao clearerAccountSnapDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPNLDao pnlDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPortfolioSnapDao portfolioSnapDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISubClearerAccountDao subClearerAccountDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISubClearerAccountSnapDao subClearerAccountSnapDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBasicMacroEventDao basicMacroEventDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGenericEntityDao genericEntityDao() {
		return null;
	}

}
