package com.activequant.interfaces.dao;

import java.util.Map;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.DaoException;

public interface IEntityDao<T extends PersistentEntity> {

    public T load(String primaryKey) throws DaoException;

    public T[] loadAll() throws DaoException;

    public String[] loadIDs() throws DaoException;
    
    public String[] findIdsLike(String pattern) throws DaoException; 

    public void create(T t) throws DaoException;

    public void delete(T t) throws DaoException;
    
    public void deleteById(String id) throws DaoException;

    public void update(T t) throws DaoException;

    /**
     * find IDs where value = key; 
     * @param key
     * @param sValue
     * @return
     */
    public String[] findIDs(String key, String sValue);

    /**
     * find IDs where value = key; 
     * @param key
     * @param sValue
     * @return
     */
    public String[] findIDs(String key, Double dValue);
    
    /**
     * find IDs where value = key; 
     * @param key
     * @param sValue
     * @return
     */
    public String[] findIDs(String key, Long lValue);
    
    public  Map<String, Object> loadRaw(String key);
    
    public  void storeRaw(String key, Map<String, Object> rawMap);

    
    public String[] findIDs(int startIndex, int endIndex);

    int count();
    
    int countForAttributeValue(String key, String sValue);

    int countForAttributeValue(String key, Double dValue);

    int countForAttributeValue(String key, Long lValue);
    
    public String[] findIDsWhereCreationDateBetween(TimeStamp startTs, TimeStamp endTs);
}
