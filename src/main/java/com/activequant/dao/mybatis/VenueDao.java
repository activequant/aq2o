package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.IVenueDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Venue;

public class VenueDao extends GenericMapperDao<Venue> implements IVenueDao {

    private static final String tableName = "Venue";

    public VenueDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, Venue.class, tableName);
    }
}
