package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Region;
import com.activequant.interfaces.dao.IRegionDao;

public class RegionDao extends GenericMapperDao<Region> implements IRegionDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "Region";

    public RegionDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, Region.class, tableName);
    }

}
