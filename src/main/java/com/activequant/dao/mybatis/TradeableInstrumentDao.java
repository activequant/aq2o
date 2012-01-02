package com.activequant.dao.mybatis;

import java.util.ArrayList;
import java.util.List;

import com.activequant.dao.ITradeableInstrumentDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.TradeableInstrument;

public class TradeableInstrumentDao extends GenericMapperDao<TradeableInstrument> implements ITradeableInstrumentDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "TradeableInstrument";

    public TradeableInstrumentDao(GenericRowMapper mapper) {
        super(mapper, TradeableInstrument.class, tableName);
    }

    public TradeableInstrument[] findFor(Instrument instrument) {
        // dirty. would have to use a field mapper table to ensure that
        // INSTRUMENTID is always the same as in MarketDataInstrument.
        // trade-off.

        List<String> ids = mapper.findByString(tableName, "INSTRUMENTID", instrument.getId());
        List<TradeableInstrument> mdis = new ArrayList<TradeableInstrument>();
        for (String id : ids) {
            mdis.add(this.load(id));
        }
        return mdis.toArray(new TradeableInstrument[] {});
    }

}
