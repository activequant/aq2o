package com.activequant.dao.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.BasicMacroEvent;
import com.activequant.interfaces.dao.IBasicMacroEventDao;

public class BasicMacroEventDao extends GenericMapperDao<BasicMacroEvent>
		implements IBasicMacroEventDao {

	private static final String tableName = "BasicMacroEvent";

	public BasicMacroEventDao(GenericRowMapper mapper, SqlSessionFactory s) {
		super(s, mapper, BasicMacroEvent.class, tableName);
	}

}
