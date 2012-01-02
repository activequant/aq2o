package com.activequant.dao.mybatis;

import com.activequant.dao.IPositionDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Position;

public class PositionDao extends GenericMapperDao<Position> implements IPositionDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "Position";

    public PositionDao(GenericRowMapper mapper) {
        super(mapper, Position.class, tableName);
    }

}
