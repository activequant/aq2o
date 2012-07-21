package com.activequant.dao.mybatis;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.interfaces.dao.IEntityDao;

public abstract class EntityDao<T extends PersistentEntity> implements IEntityDao<T> {

    public abstract T load(String primaryKey);

    public abstract T[] loadAll();

    public abstract void create(T t);

}
