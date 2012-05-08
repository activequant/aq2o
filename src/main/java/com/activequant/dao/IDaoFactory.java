package com.activequant.dao;

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
}
