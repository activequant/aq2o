package com.activequant.dao.mybatis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.backoffice.ClearedTrade;

public class ClearedTradeDao extends GenericMapperDao<ClearedTrade> implements IClearedTradeDao {

    private static final String tableName = "ClearedTrade";

    public ClearedTradeDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, ClearedTrade.class, tableName);
    }

    /* (non-Javadoc)
	 * @see com.activequant.dao.mybatis.IClearedTradeDao#loadForDay(java.util.Date)
	 */
    @Override
	public ClearedTrade[] loadForDay(Date date){
    	Calendar cal = GregorianCalendar.getInstance();
    	cal.setTime(date);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	long starttime = cal.getTimeInMillis() * 1000 * 1000;
    	cal.add(Calendar.DATE, 1);
    	long endTime = (cal.getTimeInMillis() * 1000 * 1000);
    	TimeStamp ts= new TimeStamp(endTime);
    	System.out.println(ts.getDate());
    	String[] ids = super.findIDsWhereLongValBetween("TIMESTAMPINNANOS", starttime, endTime);
    	ClearedTrade[] ret = new ClearedTrade[ids.length];
    	for(int i=0;i<ret.length;i++){
    		ret[i] = super.load(ids[i]);
    	}
    	return ret; 
    }
    
}
