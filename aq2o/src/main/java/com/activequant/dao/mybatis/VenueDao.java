package com.activequant.dao.mybatis;

import com.activequant.dao.IVenueDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Venue;

public class VenueDao extends GenericMapperDao<Venue> implements IVenueDao {

    // private Logger log = Logger.getLogger(VenueDao.class);
    private static final String tableName = "Venue";

    public VenueDao(GenericRowMapper mapper) {
        super(mapper, Venue.class, tableName);
    }
}
