package com.activequant.interfaces.dao;

import com.activequant.domainmodel.backoffice.Account;

public interface IAccountDao extends IEntityDao<Account>{

	public abstract Account[] loadAll(@SuppressWarnings("rawtypes") Class clazz);

}