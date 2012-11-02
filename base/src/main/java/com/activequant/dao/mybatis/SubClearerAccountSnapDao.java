package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.backoffice.SubClearerAccountSnap;

public class SubClearerAccountSnapDao extends AbstractSnapshotDao<SubClearerAccountSnap> implements ISubClearerAccountSnapDao {

    private static final String tableName = "SubCashAccountSnap";

    public SubClearerAccountSnapDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(mapper, SubClearerAccountSnap.class, tableName, s);
    }
    
}
