package com.activequant.interfaces.dao;


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

	IAccountDao accountDao();

	IClearedTradeDao clearedTradeDao();

	IClearerAccountSnapDao clearerAccountSnapDao();

	IPNLDao pnlDao();

	IPortfolioSnapDao portfolioSnapDao();

	ISubClearerAccountDao subClearerAccountDao();

	ISubClearerAccountSnapDao subClearerAccountSnapDao();
	
	IBasicMacroEventDao basicMacroEventDao();
	
	IGenericEntityDao genericEntityDao();

}
