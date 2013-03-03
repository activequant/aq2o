package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Country;
import com.activequant.domainmodel.GenericEntity;
import com.activequant.interfaces.dao.ICountryDao;
import com.activequant.interfaces.dao.IGenericEntityDao;

public class GenericEntityDao extends GenericMapperDao<GenericEntity> implements IGenericEntityDao {

    private static final String tableName = "GenericEntity";

    public GenericEntityDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, GenericEntity.class, tableName);
    }

}
