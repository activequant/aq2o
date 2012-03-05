package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.IPortfolioDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Portfolio;

public class PortfolioDao extends GenericMapperDao<Portfolio> implements IPortfolioDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "Portfolio";

    public PortfolioDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, Portfolio.class, tableName);
    }

}
