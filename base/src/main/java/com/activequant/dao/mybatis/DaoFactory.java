package com.activequant.dao.mybatis;

import org.springframework.beans.factory.annotation.Autowired;

import com.activequant.interfaces.dao.IAdjustedSeriesDateEntryDao;
import com.activequant.interfaces.dao.ICountryDao;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.IInstrumentDao;
import com.activequant.interfaces.dao.IMarketDataInstrumentDao;
import com.activequant.interfaces.dao.IOrderEventDao;
import com.activequant.interfaces.dao.IOrderFillDao;
import com.activequant.interfaces.dao.IPandSDao;
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

    private final ICountryDao countryDao;
    private final IRegionDao regionDao;
    private final IInstrumentDao instrumentDao;
    private final IVenueDao venueDao;
    private final ITradeableInstrumentDao tradInstDao;
    private final IMarketDataInstrumentDao mdiDao;
    private final IPositionDao positionDao;
    private final IPortfolioDao portfolioDao;
    private final ISecurityChainDao securityChainDao;
    private final IAdjustedSeriesDateEntryDao adjSDao;
    private final IPerformanceReportDao perfReportDao;
    private final IReportDao reportDao; 
    private final IOrderEventDao orderEventDao;
    
    // new interfaces. 
    @Autowired
    private IOrderFillDao orderFillDao;
    
	@Autowired
    private IPandSDao pAndSDao; 
	@Autowired
	private IAccountDao accountDao;
	

	@Autowired
	private IClearedTradeDao clearedTradeDao;
	@Autowired
	private IClearerAccountSnapDao clearerAccountSnapDao;
	@Autowired
	private IPNLDao pnlDao;
	@Autowired
	private IPortfolioSnapDao portfolioSnapDao;
	@Autowired
	private ISubClearerAccountDao subClearerAccountDao;
	@Autowired
	private ISubClearerAccountSnapDao subClearerAccountSnapDao; 
	
    
    
    // 
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

    public IOrderFillDao orderFillDao() {
		return orderFillDao;
	}

	public IPandSDao pAndSDao() {
		return pAndSDao;
	}
	
	public IAccountDao accountDao() {
		return accountDao;
	}

	public IClearedTradeDao clearedTradeDao() {
		return clearedTradeDao;
	}

	public IClearerAccountSnapDao clearerAccountSnapDao() {
		return clearerAccountSnapDao;
	}

	public IPNLDao pnlDao() {
		return pnlDao;
	}

	public IPortfolioSnapDao portfolioSnapDao() {
		return portfolioSnapDao;
	}

	public ISubClearerAccountDao subClearerAccountDao() {
		return subClearerAccountDao;
	}

	public ISubClearerAccountSnapDao subClearerAccountSnapDao() {
		return subClearerAccountSnapDao;
	}
}
