package com.activequant.interfaces.dao;

import com.activequant.dao.mybatis.IAccountDao;
import com.activequant.dao.mybatis.IClearedTradeDao;
import com.activequant.dao.mybatis.IClearerAccountSnapDao;
import com.activequant.dao.mybatis.IPNLDao;
import com.activequant.dao.mybatis.IPortfolioSnapDao;
import com.activequant.dao.mybatis.ISubClearerAccountDao;
import com.activequant.dao.mybatis.ISubClearerAccountSnapDao;

public interface IDaoFactory {
	ICountryDao countryDao();

	IRegionDao regionDao();

	IInstrumentDao instrumentDao();

	IVenueDao venueDao();

	IMarketDataInstrumentDao mdiDao();

	ITradeableInstrumentDao tradeableDao();

	IPositionDao positionDao();

	IPortfolioDao portfolioDao();

	ISecurityChainDao securityChainDao();

	IAdjustedSeriesDateEntryDao adjSerDtEntryDao();

	IPerformanceReportDao perfDao();

	IReportDao reportDao();

	IOrderEventDao orderEventDao();

	IOrderFillDao orderFillDao();

	IPandSDao pAndSDao();

	public IAccountDao accountDao();

	public IClearedTradeDao clearedTradeDao();

	public IClearerAccountSnapDao clearerAccountSnapDao();

	public IPNLDao pnlDao();

	public IPortfolioSnapDao portfolioSnapDao();

	public ISubClearerAccountDao subClearerAccountDao();

	public ISubClearerAccountSnapDao subClearerAccountSnapDao();

}
