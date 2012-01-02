package com.activequant.dao.mybatis;

import com.activequant.dao.ICountryDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Country;

public class CountryDao extends GenericMapperDao<Country> implements ICountryDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "Country";

    public CountryDao(GenericRowMapper mapper) {
        super(mapper, Country.class, tableName);
    }

}
