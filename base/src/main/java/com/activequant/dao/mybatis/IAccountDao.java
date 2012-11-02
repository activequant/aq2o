package com.activequant.dao.mybatis;

import com.activequant.domainmodel.backoffice.Account;
import com.activequant.interfaces.dao.IEntityDao;

public interface IAccountDao extends IEntityDao<Account>{

	public abstract Account[] loadAll(@SuppressWarnings("rawtypes") Class clazz);

}