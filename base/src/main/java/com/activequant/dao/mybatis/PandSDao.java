package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.backoffice.PandS;
import com.activequant.interfaces.dao.IPandSDao;

public class PandSDao extends GenericMapperDao<PandS> implements IPandSDao {

    private static final String tableName = "PnS";

    public PandSDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, PandS.class, tableName);
    }

}
