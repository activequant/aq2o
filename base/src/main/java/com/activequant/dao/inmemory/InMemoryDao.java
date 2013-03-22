package com.activequant.dao.inmemory;

import java.util.Map;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.interfaces.dao.IEntityDao;

public class InMemoryDao<T extends PersistentEntity> implements IEntityDao<T> {

	@Override
	public T load(String primaryKey) throws DaoException {
		return null;
	}

	@Override
	public T[] loadAll() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] loadIDs() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] findIdsLike(String pattern) throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(T t) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(T t) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(String id) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(T t) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] findIDs(String key, String sValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] findIDs(String key, Double dValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] findIDs(String key, Long lValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] findIDs(int startIndex, int endIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countForAttributeValue(String key, String sValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countForAttributeValue(String key, Double dValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int countForAttributeValue(String key, Long lValue) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] findIDsWhereCreationDateBetween(TimeStamp startTs, TimeStamp endTs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public  Map<String, Object> loadRaw(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeRaw(String key, Map<String, Object> rawMap) {
		// TODO Auto-generated method stub
		
	}

}
