package com.activequant.dao.mybatis;

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

    public DaoFactory(ICountryDao countryDao, IRegionDao regionDao, IInstrumentDao instrDao, IVenueDao venueDao,
            ITradeableInstrumentDao tradInstDao, IMarketDataInstrumentDao mdiDao, IPositionDao positionDao,
            IPortfolioDao portfolioDao, ISecurityChainDao securityChainDao) {
        this.countryDao = countryDao;
        this.regionDao = regionDao;
        this.instrumentDao = instrDao;
        this.venueDao = venueDao;
        this.mdiDao = mdiDao;
        this.tradInstDao = tradInstDao;
        this.positionDao = positionDao;
        this.portfolioDao = portfolioDao;
        this.securityChainDao = securityChainDao; 
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
    
}
