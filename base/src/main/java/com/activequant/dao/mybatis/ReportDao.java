package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Report;
import com.activequant.interfaces.dao.IReportDao;

public class ReportDao extends GenericMapperDao<Report> implements IReportDao {

    // private Logger log = Logger.getLogger(InstrumentDao.class);
    private static final String tableName = "Report";

    public ReportDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, Report.class, tableName);
    }

}
