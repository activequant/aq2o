package com.activequant.interfaces.dao;

import java.util.Date;

import com.activequant.domainmodel.backoffice.ClearedTrade;

public interface IClearedTradeDao extends IEntityDao<ClearedTrade>{

	public abstract ClearedTrade[] loadForDay(Date date);

}