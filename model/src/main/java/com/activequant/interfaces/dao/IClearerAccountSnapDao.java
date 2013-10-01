package com.activequant.interfaces.dao;

import com.activequant.domainmodel.backoffice.ClearerAccountSnap;

public interface IClearerAccountSnapDao extends IEntityDao<ClearerAccountSnap>{

	public abstract ClearerAccountSnap load(String accountId, Long date8);

}