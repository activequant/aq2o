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

	public IAccountDao accountDao();

	public IClearedTradeDao clearedTradeDao();

	public IClearerAccountSnapDao clearerAccountSnapDao();

	public IPNLDao pnlDao();

	public IPortfolioSnapDao portfolioSnapDao();

	public ISubClearerAccountDao subClearerAccountDao();

	public ISubClearerAccountSnapDao subClearerAccountSnapDao();

}
