package com.activequant.dao.mybatis;

import com.activequant.dao.IRegionDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Region;

public class RegionDao extends GenericMapperDao<Region> implements IRegionDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "Region";

    public RegionDao(GenericRowMapper mapper) {
        super(mapper, Region.class, tableName);
    }

}
