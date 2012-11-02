package com.activequant.dao.mybatis;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.backoffice.OrderFill;
import com.activequant.interfaces.dao.IOrderFillDao;

public class OrderFillDao extends GenericMapperDao<OrderFill> implements IOrderFillDao {

    private static final String tableName = "OrderFill";

    public OrderFillDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, OrderFill.class, tableName);
    }

	/* (non-Javadoc)
	 * @see com.activequant.dao.mybatis.IOrderFillDao#loadForDay(java.util.Date)
	 */
	@Override
	public OrderFill[] loadForDay(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
		long starttime = cal.getTimeInMillis() * 1000 * 1000;
		cal.add(Calendar.DATE, 1);
		long endTime = (cal.getTimeInMillis() * 1000 * 1000);
		TimeStamp ts = new TimeStamp(endTime);
		System.out.println(ts.getDate());
		String[] ids = super.findIDsWhereLongValBetween("TIMESTAMPINNANOS", starttime, endTime);
		OrderFill[] ret = new OrderFill[ids.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = super.load(ids[i]);
		}
		return ret;
	}
}
