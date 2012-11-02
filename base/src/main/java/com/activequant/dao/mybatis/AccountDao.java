package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.backoffice.Account;

public class AccountDao extends GenericMapperDao<Account> implements IAccountDao {

    private static final String tableName = "Account";

    public AccountDao(GenericRowMapper mapper, SqlSessionFactory s) {
        super(s, mapper, Account.class, tableName);
    }
    
    /* (non-Javadoc)
	 * @see com.activequant.dao.mybatis.IAccountDao#loadAll(java.lang.Class)
	 */
    @Override
	public Account[] loadAll(@SuppressWarnings("rawtypes") Class clazz){
    	String[] ids = super.findIDs("CLASSNAME", clazz.getCanonicalName());
    	Account[] ret = new Account[ids.length];
    	for(int i=0;i<ids.length;i++)
    	{
    		ret[i] = super.load(ids[i]);
    	}    	
    	return ret;     	
    }
    
    
}
