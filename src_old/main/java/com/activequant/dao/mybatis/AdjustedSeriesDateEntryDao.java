package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.AdjustedSeriesDateEntry;
import com.activequant.interfaces.dao.IAdjustedSeriesDateEntryDao;

public class AdjustedSeriesDateEntryDao extends GenericMapperDao<AdjustedSeriesDateEntry> implements IAdjustedSeriesDateEntryDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "AdjustedSeriesDateEntry";

    public AdjustedSeriesDateEntryDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, AdjustedSeriesDateEntry.class, tableName);
    }

}
