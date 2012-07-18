package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.SecurityChain;
import com.activequant.interfaces.dao.ISecurityChainDao;

public class SecurityChainDao extends GenericMapperDao<SecurityChain> implements ISecurityChainDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "SecurityChain";

    public SecurityChainDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, SecurityChain.class, tableName);
    }

}
