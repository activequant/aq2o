package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.backoffice.PNL;

/**
 * 
 * @author GhostRider
 *
 */
public class PNLDao extends GenericMapperDao<PNL> implements IPNLDao {

    private static final String tableName = "PNL";

    public PNLDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, PNL.class, tableName);
    }

}
