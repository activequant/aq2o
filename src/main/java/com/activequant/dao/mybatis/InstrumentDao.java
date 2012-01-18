package com.activequant.dao.mybatis;

import com.activequant.dao.IInstrumentDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Instrument;

public class InstrumentDao extends GenericMapperDao<Instrument> implements IInstrumentDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "Instrument";

    public InstrumentDao(GenericRowMapper mapper) {
        super(mapper, Instrument.class, tableName);
    }

    public String[] getNonExpiredInstruments(long date8) {
        return findIDsWhereLongValGreater("EXPIRY", date8);
    }

    /**
     * Due to the use of arrays, i have to delete the entire instrument.
     */
    public void update(Instrument instrument) {
        super.delete(instrument);
        super.update(instrument);
    }

    public String[] searchById(String idsLikeString, int resultAmount) {
        return findIDsLike(idsLikeString, resultAmount);
    }


}
