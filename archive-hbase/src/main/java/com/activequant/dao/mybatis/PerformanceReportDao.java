package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.PerformanceReport;
import com.activequant.interfaces.dao.IPerformanceReportDao;

public class PerformanceReportDao extends GenericMapperDao<PerformanceReport> implements IPerformanceReportDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "PerformanceReport";

    public PerformanceReportDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, PerformanceReport.class, tableName);
    }

}
