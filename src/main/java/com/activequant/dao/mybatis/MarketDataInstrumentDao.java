package com.activequant.dao.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import com.activequant.dao.IMarketDataInstrumentDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;

public class MarketDataInstrumentDao extends GenericMapperDao<MarketDataInstrument> implements IMarketDataInstrumentDao {

    private Logger log = Logger.getLogger(MarketDataInstrumentDao.class);
    private static final String tableName = "MarketDataInstrument";

    public MarketDataInstrumentDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, MarketDataInstrument.class, tableName);
    }

    public MarketDataInstrument[] findFor(Instrument instrument) {
        // dirty. would have to use a field mapper table to ensure that
        // INSTRUMENTID is always the same as in MarketDataInstrument.
        // trade-off.

        List<String> ids = mapper.findByString(tableName, "INSTRUMENTID", instrument.getId());
        List<MarketDataInstrument> mdis = new ArrayList<MarketDataInstrument>();
        for (String id : ids) {
            mdis.add(this.load(id));
        }
        return mdis.toArray(new MarketDataInstrument[] {});
    }

    public MarketDataInstrument[] findForProvider(String providerId) {
        List<String> ids = mapper.findByString(tableName, "mdProvider".toUpperCase(), providerId);
        List<MarketDataInstrument> mdis = new ArrayList<MarketDataInstrument>();
        for (String id : ids) {
            mdis.add(this.load(id));
        }
        return mdis.toArray(new MarketDataInstrument[] {});
    }

    public MarketDataInstrument findByProvId(String providerId, String provSpecInstId) {
        List<String> insts = mapper.findBy2StringVals(tableName, "MdProvider".toUpperCase(), providerId,
                "ProviderSpecificId".toUpperCase(), provSpecInstId);
        if (insts.size() > 1) {
            throw new RuntimeException("Ambigous! "  + providerId + "/"+provSpecInstId);
        }
        if (insts.size() == 0)
            return null;
        String id = insts.get(0);
        MarketDataInstrument mdi = load(id);
        return mdi;
    }

    public int countForProvider(String providerId) {
        return countForAttributeValue("MDPROVIDER", providerId);
    }

    public MarketDataInstrument[] findForProvider(String providerId, int startIndex, int maxAmount) {
        List<String> ids = mapper.findByString(tableName, "mdProvider".toUpperCase(), providerId);
        List<MarketDataInstrument> mdis = new ArrayList<MarketDataInstrument>();
        for (int i = startIndex; i < startIndex + maxAmount; i++) {
            if (ids.size() > i)
                mdis.add(this.load(ids.get(i)));
        }
        return mdis.toArray(new MarketDataInstrument[] {});
    }

    public MarketDataInstrument[] findLike(String providerId, String provSpecInstId) {
        List<String> ids = mapper.findByString(tableName, "mdProvider".toUpperCase(), providerId);
        List<MarketDataInstrument> mdis = new ArrayList<MarketDataInstrument>();
        for (int i = 0; i < ids.size(); i++) {
            MarketDataInstrument mdi = this.load(ids.get(i));
            if (mdi.getProviderSpecificId().startsWith(provSpecInstId))
                mdis.add(mdi);
        }
        return mdis.toArray(new MarketDataInstrument[] {});
    }

	@Override
	public MarketDataInstrument findFor(String providerId, Instrument instrument) {
        List<String> insts = mapper.findBy2StringVals(tableName, "MdProvider".toUpperCase(), providerId,
                "instrumentId".toUpperCase(), instrument.getId());
        if (insts.size() > 1) {
            throw new RuntimeException("Ambigous!" + providerId + "/"+instrument.getId());
        }
        if (insts.size() == 0){
        	log.warn("Did not find instrument " + instrument.getId() + " for provider " + providerId);
            return null;
        }
        String id = insts.get(0);
        MarketDataInstrument mdi = load(id);
        return mdi;
	}

    @Override
    public String[] getProviders() {      
        return super.selectDistinctStringVal("MDPROVIDER");
    }

}
