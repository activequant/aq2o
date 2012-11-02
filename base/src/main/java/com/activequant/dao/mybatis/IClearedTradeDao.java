package com.activequant.dao.mybatis;

import java.util.Date;

import com.activequant.domainmodel.backoffice.ClearedTrade;
import com.activequant.interfaces.dao.IEntityDao;

public interface IClearedTradeDao extends IEntityDao<ClearedTrade>{

	public abstract ClearedTrade[] loadForDay(Date date);

}