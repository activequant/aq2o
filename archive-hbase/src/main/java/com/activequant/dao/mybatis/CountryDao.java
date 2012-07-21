package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Country;
import com.activequant.interfaces.dao.ICountryDao;

public class CountryDao extends GenericMapperDao<Country> implements ICountryDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "Country";

    public CountryDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, Country.class, tableName);
    }

}
