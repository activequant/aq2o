package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.backoffice.SubClearerAccount;
import com.activequant.interfaces.dao.ISubClearerAccountDao;

public class SubClearerAccountDao extends GenericMapperDao<SubClearerAccount> implements ISubClearerAccountDao {

    private static final String tableName = "SubCashAccount";

    public SubClearerAccountDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, SubClearerAccount.class, tableName);
    }

}
