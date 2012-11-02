package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.backoffice.PortfolioSnap;
import com.activequant.interfaces.dao.IPortfolioSnapDao;

public class PortfolioSnapDao extends AbstractSnapshotDao<PortfolioSnap> implements IPortfolioSnapDao {

    private static final String tableName = "PortfolioSnap";

    public PortfolioSnapDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(mapper, PortfolioSnap.class, tableName, s);
    }
    
}
