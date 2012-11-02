package com.activequant.dao.mybatis;

import java.lang.reflect.Array;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.GenericMapperDao;
import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeStamp;

public abstract class AbstractSnapshotDao<T extends PersistentEntity> extends GenericMapperDao<T> {

    private final String tableName;
    private final Class<T> clazz;

    public AbstractSnapshotDao(GenericRowMapper mapper, Class<T> clazz, String tableName, SqlSessionFactory s) {
    	super(s, mapper, clazz, tableName);
    	this.clazz = clazz;
        this.tableName = tableName; 
    }
    
    public T loadSnapshot(String nonUniqueID, TimeStamp when){
        String id = super.findLastIdBeforeCreationTime("NONUNIQUEID", nonUniqueID, when.getNanoseconds());
        if(id!=null){
            return super.load(id);
        }
        return null; 
    }
        
    @SuppressWarnings("unchecked")
	public T[] loadSnapshots(String nonUniqueID, TimeStamp start, TimeStamp end){
        List<String> ids= super.findIDsBetweenCreationTime("NONUNIQUEID", nonUniqueID, start.getNanoseconds(), end.getNanoseconds());
        if(ids.size()>0){
			T[] ret = (T[])Array.newInstance(clazz, ids.size());
            for(int i =0;i<ret.length;i++){
                ret[i] = super.load(ids.get(i));
            }
            return ret; 
        }
        return (T[])Array.newInstance(clazz, 0);
    }

}
