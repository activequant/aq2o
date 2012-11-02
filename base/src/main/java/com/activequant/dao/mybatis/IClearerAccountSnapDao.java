package com.activequant.dao.mybatis;

import com.activequant.domainmodel.backoffice.ClearerAccountSnap;
import com.activequant.interfaces.dao.IEntityDao;

public interface IClearerAccountSnapDao extends IEntityDao<ClearerAccountSnap>{

	public abstract ClearerAccountSnap load(String accountId, Long date8);

}