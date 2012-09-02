package com.activequant.dao.mybatis;

import com.activequant.interfaces.dao.IAdjustedSeriesDateEntryDao;
import com.activequant.interfaces.dao.ICountryDao;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.IInstrumentDao;
import com.activequant.interfaces.dao.IMarketDataInstrumentDao;
import com.activequant.interfaces.dao.IOrderEventDao;
import com.activequant.interfaces.dao.IPerformanceReportDao;
import com.activequant.interfaces.dao.IPortfolioDao;
import com.activequant.interfaces.dao.IPositionDao;
import com.activequant.interfaces.dao.IRegionDao;
import com.activequant.interfaces.dao.IReportDao;
import com.activequant.interfaces.dao.ISecurityChainDao;
import com.activequant.interfaces.dao.ITradeableInstrumentDao;
import com.activequant.interfaces.dao.IVenueDao;

/**
 * MyBatis implementation of the DAO factory interface.
 * 
 * @author ustaudinger
 * 
 */
public class DaoFactory implements IDaoFactory {

    private ICountryDao countryDao;
    private IRegionDao regionDao;
    private IInstrumentDao instrumentDao;
    private IVenueDao venueDao;
    private ITradeableInstrumentDao tradInstDao;
    private IMarketDataInstrumentDao mdiDao;
    private IPositionDao positionDao;
    private IPortfolioDao portfolioDao;
    private ISecurityChainDao securityChainDao;
    private IAdjustedSeriesDateEntryDao adjSDao;
    private IPerformanceReportDao perfReportDao;
    private IReportDao reportDao; 
    private IOrderEventDao orderEventDao;

    public DaoFactory(ICountryDao countryDao, IRegionDao regionDao, IInstrumentDao instrDao, IVenueDao venueDao,
            ITradeableInstrumentDao tradInstDao, IMarketDataInstrumentDao mdiDao, IPositionDao positionDao,
            IPortfolioDao portfolioDao, ISecurityChainDao securityChainDao, IAdjustedSeriesDateEntryDao aD, 
            IPerformanceReportDao perfReportDao, IReportDao reportDao, IOrderEventDao orderEventDao) {
        this.countryDao = countryDao;
        this.regionDao = regionDao;
        this.instrumentDao = instrDao;
        this.venueDao = venueDao;
        this.mdiDao = mdiDao;
        this.tradInstDao = tradInstDao;
        this.positionDao = positionDao;
        this.portfolioDao = portfolioDao;
        this.securityChainDao = securityChainDao;
        this.adjSDao = aD;
        this.perfReportDao = perfReportDao;
        this.reportDao = reportDao; 
        this.orderEventDao = orderEventDao; 
    }

    public ICountryDao countryDao() {
        return countryDao;
    }

    public IRegionDao regionDao() {
        return regionDao;
    }

    public IInstrumentDao instrumentDao() {
        return instrumentDao;
    }

    public IVenueDao venueDao() {
        return venueDao;
    }

    public IMarketDataInstrumentDao mdiDao() {
        return mdiDao;
    }

    public ITradeableInstrumentDao tradeableDao() {
        return tradInstDao;
    }

    public IPortfolioDao portfolioDao() {
        return portfolioDao;
    }

    public IPositionDao positionDao() {
        return positionDao;
    }
    
    public ISecurityChainDao securityChainDao(){
    	return securityChainDao; 
    }

	@Override
	public IAdjustedSeriesDateEntryDao adjSerDtEntryDao() {
		return adjSDao;
	}

	@Override
	public IPerformanceReportDao perfDao() {
		return perfReportDao;
	}

	@Override
	public IReportDao reportDao() {
		return reportDao;
	}

	@Override
	public IOrderEventDao orderEventDao() {
		return orderEventDao;
	}
    
}
