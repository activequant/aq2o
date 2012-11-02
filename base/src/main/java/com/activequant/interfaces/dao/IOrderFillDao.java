package com.activequant.interfaces.dao;

import java.util.Date;

import com.activequant.domainmodel.backoffice.OrderFill;
import com.activequant.domainmodel.trade.event.OrderEvent;

public interface IOrderFillDao extends IEntityDao<OrderFill> {

	public abstract OrderFill[] loadForDay(Date date);

}