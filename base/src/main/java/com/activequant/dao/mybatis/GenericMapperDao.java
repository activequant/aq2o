package com.activequant.dao.mybatis;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import com.activequant.dao.mybatis.mapper.GenericRowMapper;
import com.activequant.domainmodel.GenericRow;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.DaoException;

/**
 * 
 * @author ustaudinger
 * 
 * @param <T>
 */
public class GenericMapperDao<T extends PersistentEntity> {

	private Logger log = Logger.getLogger(GenericMapperDao.class);
	protected GenericRowMapper mapper;
	private String tableName;
	private Class<? extends PersistentEntity> clazz;
	private SqlSessionFactory sqlSessionFactory;

	public GenericMapperDao(SqlSessionFactory sqlSessionFactory,
			GenericRowMapper mapper, Class<? extends PersistentEntity> clazz,
			String table) {
		log.info("Initializing GenericDao for table " + table);
		this.tableName = table;
		this.sqlSessionFactory = sqlSessionFactory;
		this.mapper = mapper;
		this.clazz = clazz;
		// don't know how to check with ibatis if a table exists - at least not
		// in an easy w.
		// this one is easier, dirty and safe.
		// SqlSession sqlSession = sqlSessionFactory.openSession();
		// sqlSession.select("SHOW INDEX FROM " + table, new ResultHandler(){
		// @Override
		// public void handleResult(ResultContext arg0) {
		// System.out.println(arg0);
		// }});
		// sqlSession.close();
		try {
			mapper.init(table);
			mapper.genIndex1(table);
			mapper.genIndex2(table);
			mapper.genIndex3(table);
			mapper.genIndex4(table);
			mapper.genIndex5(table);
			mapper.genIndex6(table);
			mapper.genIndex7(table);
			mapper.genIndex8(table);
			mapper.genKey9(table);
		} catch (Exception ex) {
			log.info("Error creating table, possibly it exists already. "
					+ ex.getStackTrace()[0]);
		}
	}

	
	public Map<String, Object> loadRaw(String primaryKey) {
		List<GenericRow> rows = mapper.load(tableName, primaryKey);
		// to be offloaded to a generic class.
		Map<String, Object> map = new HashMap<String, Object>();
		for (GenericRow row : rows) {
			String fieldName = row.getFieldName();
			if (row.getDoubleVal() != null)
				map.put(fieldName, row.getDoubleVal());
			else if (row.getLongVal() != null)
				map.put(fieldName, row.getLongVal());
			else if (row.getStringVal() != null)
				map.put(fieldName, row.getStringVal());
		}
		return map; 
	}

	//
	@SuppressWarnings("unchecked")
	public T load(String primaryKey) {

		List<GenericRow> rows = mapper.load(tableName, primaryKey);

		// to be offloaded to a generic class.
		Map<String, Object> map = new HashMap<String, Object>();
		for (GenericRow row : rows) {
			String fieldName = row.getFieldName();
			if (row.getDoubleVal() != null)
				map.put(fieldName, row.getDoubleVal());
			else if (row.getLongVal() != null)
				map.put(fieldName, row.getLongVal());
			else if (row.getStringVal() != null)
				map.put(fieldName, row.getStringVal());
		}
		String className = (String) map.get("ClassName".toUpperCase());
		if (className == null)
			// no valid entry.
			return null;
		T ret = null;
		try {
			@SuppressWarnings({ "rawtypes" })
			Class clazz = Class.forName(className);
			ret = (T) clazz.newInstance();
			ret.initFromMap(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * First sentence: Use with care. This will load ALL object instances from
	 * DB, which could possibly become a very large table.
	 * 
	 * TODO: find another way to load things, this is too slow.
	 * 
	 * @return array of ALL instances in persistence layer.
	 */
	@SuppressWarnings("unchecked")
	public T[] loadAll() {
		List<String> iids = mapper.loadKeyList(tableName);
		T[] ret = (T[]) Array.newInstance(clazz, iids.size());
		for (int i = 0; i < iids.size(); i++) {
			ret[i] = load(iids.get(i));
		}
		return ret;
	}

	public synchronized void delete(T t) {
		mapper.delete(tableName, t.getId());
	}

	public synchronized void deleteById(String id) {
		mapper.delete(tableName, id);
	}

	public synchronized void update(T t) {
		this.delete(t);
		this.create(t);
	}

	public String[] findIDsWhereCreationDateBetween(TimeStamp startTs,
			TimeStamp endTs) {
		List<String> ids = mapper.findIDsBetween(tableName,
				startTs.getMilliseconds(), endTs.getMilliseconds());
		return ids.toArray(new String[] {});

	}

	/**
	 * Use this function to load all IDs
	 * 
	 * @return a list of all Key-IDs in this set.
	 * 
	 */
	public String[] loadIDs() {
		List<String> iids = mapper.loadKeyList(tableName);
		return iids.toArray(new String[] {});
	}

	public String[] findIdsLike(String pattern) throws DaoException {
		List<String> ret = mapper.findIdsLike(tableName, pattern,
				Integer.MAX_VALUE);
		return ret.toArray(new String[] {});
	}

	private GenericRow genRow(long createdTimeStamp, String id, String key,
			Object value) {
		GenericRow gr = null;
		if (value instanceof Double) {
			gr = new GenericRow(createdTimeStamp, id, key, null,
					(Double) value, null);
		} else if (value instanceof String) {
			gr = new GenericRow(createdTimeStamp, id, key, null, null,
					(String) value);
		} else if (value instanceof Integer) {
			gr = new GenericRow(createdTimeStamp, id, key,
					((Integer) value).longValue(), null, null);
		} else if (value instanceof Long) {
			gr = new GenericRow(createdTimeStamp, id, key, (Long) value, null,
					null);
		}
		return gr;
	}

	private List<GenericRow> createGenRows(T t) {
		List<GenericRow> ret = new ArrayList<GenericRow>();
		// TODO Auto-generated method stub
		Map<String, Object> map = t.propertyMap();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			boolean added = false;
			String key = it.next();
			Object value = map.get(key);
			if (value == null)
				continue;
			GenericRow gr = null;
			long createdTimeStamp = t.getCreationTime();
			if (value instanceof Object[]) {
				// generate individual generic rows for all entries of this
				// array.
				Object[] array = (Object[]) value;
				int totalLength = array.length;
				for (int i = 0; i < totalLength; i++) {
					String fieldName = "[" + key + ";" + i + ";" + totalLength;
					GenericRow temp = genRow(createdTimeStamp, t.getId(),
							fieldName, array[i]);
					ret.add(temp);
					added = true;
				}
			} else
				gr = genRow(createdTimeStamp, t.getId(), key, value);

			if (gr != null)
				ret.add(gr);
			else if (!added)
				log.warn("NO VALUE CONVERTER FOR VALUE: " + key + "/ " + value);
		}

		return ret;
	}

	public void create(T t) {
		SqlSession sqlSession = sqlSessionFactory
				.openSession(ExecutorType.BATCH);
		try {
			List<GenericRow> rows = createGenRows(t);
			for (GenericRow row : rows) {
				mapper.insert(tableName, row);
			}
			sqlSession.commit();
		} finally {
			sqlSession.close();
		}

	}

	public String[] findIDs(String key, String sValue) {
		List<String> ret = mapper.findByString(tableName, key, sValue);
		return ret.toArray(new String[] {});
	}

	public String[] findIDsWhereLongValGreater(String fieldName, long sValue) {
		List<String> ret = mapper.findIDsWhereLongValGreater(tableName,
				fieldName, sValue);
		return ret.toArray(new String[] {});
	}

	public String[] findIDsWhereLongValBetween(String fieldName, long minValue,
			long maxValue) {
		List<String> ret = mapper.findIDsWhereLongValBetween(tableName,
				fieldName, minValue, maxValue);
		return ret.toArray(new String[] {});
	}

	public String[] findIDs(String key, Double dValue) {
		List<String> ret = mapper.findByDouble(tableName, key, dValue);
		return ret.toArray(new String[] {});
	}

	public String[] findIDs(String key, Long lValue) {
		List<String> ret = mapper.findByLong(tableName, key, lValue);
		return ret.toArray(new String[] {});
	}

	public String[] findIDs(int startIndex, int endIndex) {
		List<String> ret = mapper.findIDs(tableName, startIndex, endIndex);
		return ret.toArray(new String[] {});
	}

	public String[] findIDsLike(String idsLikeString, int resultAmount) {
		List<String> ret = mapper.findIdsLike(tableName, idsLikeString,
				resultAmount);
		return ret.toArray(new String[] {});
	}

	public int count() {
		return mapper.count(tableName);
	}

	public int countForAttributeValue(String key, String value) {
		return mapper.countForStringValue(tableName, key, value);
	}

	public int countForAttributeValue(String key, Double value) {
		return mapper.countForDoubleValue(tableName, key, value);
	}

	public int countForAttributeValue(String key, Long value) {
		return mapper.countForLongValue(tableName, key, value);
	}

	public String[] selectDistinctStringVal(String val) {
		List<String> ret = mapper.selectDistinctStringVal(tableName, val);
		return ret.toArray(new String[] {});
	}

	public Long[] selectDistinctLongVal(String val) {
		List<Long> ret = mapper.selectDistinctLongVal(tableName, val);
		return ret.toArray(new Long[] {});
	}

	public Double[] selectDistinctDoubleVal(String val) {
		List<Double> ret = mapper.selectDistinctDoubleVal(tableName, val);
		return ret.toArray(new Double[] {});
	}

	public List<String> findIDsBetweenCreationTime(String parameter,
			String value, Long fromTimeStampInMs, Long toMs) {
		List<String> ids = mapper.findIDsBetweenCreationTime(tableName,
				parameter, value, fromTimeStampInMs, toMs);
		return ids;
	}

	public String findLastIdBeforeCreationTime(String parameter, String value,
			Long timeStampInMs) {
		return mapper.findLastIdBeforeCreationTime(tableName, parameter, value,
				timeStampInMs);
	}

}
