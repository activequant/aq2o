package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.Country;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.interfaces.dao.IOrderEventDao;

public class OrderEventDao extends GenericMapperDao<OrderEvent> implements IOrderEventDao {

    private static final String tableName = "OrderEvents";

    public OrderEventDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, Country.class, tableName);
    }

}
