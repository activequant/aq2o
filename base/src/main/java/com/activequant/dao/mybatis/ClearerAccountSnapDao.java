package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.backoffice.ClearerAccountSnap;
import com.activequant.interfaces.dao.IClearerAccountSnapDao;

public class ClearerAccountSnapDao extends AbstractSnapshotDao<ClearerAccountSnap> implements IClearerAccountSnapDao {

    private static final String tableName = "ClearerAccountSnap";

    public ClearerAccountSnapDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(mapper, ClearerAccountSnap.class, tableName, s);
    }
    
    /* (non-Javadoc)
	 * @see com.activequant.dao.mybatis.IClearerAccountSnaoDao#load(java.lang.String, java.lang.Long)
	 */
    @Override
	public ClearerAccountSnap load(String accountId, Long date8){
    	return null; 
    }
    
    
    
}
