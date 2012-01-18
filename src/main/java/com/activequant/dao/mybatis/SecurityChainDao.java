package com.activequant.dao.mybatis;

import com.activequant.dao.ISecurityChainDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.SecurityChain;

public class SecurityChainDao extends GenericMapperDao<SecurityChain> implements ISecurityChainDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "SecurityChain";

    public SecurityChainDao(GenericRowMapper mapper) {
        super(mapper, SecurityChain.class, tableName);
    }

}
