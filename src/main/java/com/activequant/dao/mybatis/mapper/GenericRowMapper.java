package com.activequant.dao.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;

import com.activequant.domainmodel.GenericRow;

@CacheNamespace(size = 100000, flushInterval = (15 * 60 * 1000))
public interface GenericRowMapper {

    @Select("create table ${table} (created bigint not null, keyVal varchar(200) not null, fieldName varchar(200) not null, doubleVal double, longVal bigint, stringVal varchar(200))")
    void init(@Param("table") String table);

    @Select("CREATE INDEX i1 on ${table} (keyVal, fieldName)")
    void genIndex1(@Param("table") String table);

    @Select("CREATE INDEX i2 on ${table} (keyVal)")
    void genIndex2(@Param("table") String table);

    @Select("CREATE INDEX i3 on ${table} (keyVal, fieldName, doubleVal)")
    void genIndex3(@Param("table") String table);

    @Select("CREATE INDEX i4 on ${table} (keyVal, fieldName, stringVal)")
    void genIndex4(@Param("table") String table);

    @Select("CREATE INDEX i5 on ${table} (keyVal, fieldName, longVal)")
    void genIndex5(@Param("table") String table);

    @Select("CREATE INDEX i6 on ${table} (fieldName, longVal)")
    void genIndex6(@Param("table") String table);

    @Select("CREATE INDEX i7 on ${table} (fieldName, doubleVal)")
    void genIndex7(@Param("table") String table);

    @Select("CREATE INDEX i8 on ${table} (fieldName, stringVal)")
    void genIndex8(@Param("table") String table);

    @Select("ALTER TABLE ${table} ADD PRIMARY KEY (fieldName, keyVal)")
    void genKey9(@Param("table") String table);

    @Select("select * from ${table} where keyVal = #{keyValue} order by fieldName ASC")
    @Results({ @Result(property = "created", column = "created"), @Result(property = "keyVal", column = "keyVal"),
            @Result(property = "fieldName", column = "fieldName"),
            @Result(property = "doubleVal", column = "doubleVal"), @Result(property = "longVal", column = "longVal"),
            @Result(property = "stringVal", column = "stringVal")

    })
    List<GenericRow> load(@Param("table") String table, @Param("keyValue") String keyValue);

    @Transactional
    @Insert("insert into ${table} values (#{row.created}, #{row.keyVal},#{row.fieldName},#{row.doubleVal},#{row.longVal},#{row.stringVal})")
    @Options(flushCache = true)
    void insert(@Param("table") String table, @Param("row") GenericRow row);

    @Select("SELECT distinct(keyVal) from ${table}")
    List<String> loadKeyList(@Param("table") String table);

    @Select("SELECT keyVal from ${table} where fieldName=#{fieldName} and stringVal=#{val}")
    List<String> findByString(@Param("table") String table, @Param("fieldName") String fieldName,
            @Param("val") String val);

    @Select("SELECT keyVal from ${table} where fieldName=#{fieldName} and longVal=#{val}")
    List<String> findByLong(@Param("table") String table, @Param("fieldName") String fieldName, @Param("val") Long val);

    @Select("SELECT keyVal from ${table} where fieldName=#{fieldName} and doubleVal=#{val}")
    List<String> findByDouble(@Param("table") String table, @Param("fieldName") String fieldName,
            @Param("val") Double val);

    @Delete("DELETE FROM ${table} where keyVal=#{keyVal}")
    void delete(@Param("table") String table, @Param("keyVal") String keyVal);

    @Select("select distinct(A.keyVal) from ${table} A, ${table} B where (A.fieldName=#{fieldName1} and A.stringVal=#{val1}) and  (B.fieldName=#{fieldName2} and B.stringVal = #{val2}) and A.keyVal = B.keyVal")
    List<String> findBy2StringVals(@Param("table") String table, @Param("fieldName1") String fieldName1,
            @Param("val1") String val1, @Param("fieldName2") String fieldName2, @Param("val2") String val2);

    @Select("SELECT keyVal from ${table} where fieldName=#{fieldName} and longVal>=#{val}")
    List<String> findIDsWhereLongValGreater(@Param("table") String table, @Param("fieldName") String fieldName,
            @Param("val") Long val);

    @Select("SELECT count(distinct(keyVal)) from ${table} where fieldName=#{fieldName} and stringVal=#{val}")
    int countForStringValue(@Param("table") String table, @Param("fieldName") String fieldName,
            @Param("val") String value);
    
    @Select("SELECT count(distinct(keyVal)) from ${table} where fieldName=#{fieldName} and longVal=#{val}")
    int countForLongValue(@Param("table") String table, @Param("fieldName") String fieldName,
            @Param("val") Long value);
    
    @Select("SELECT count(distinct(keyVal)) from ${table} where fieldName=#{fieldName} and doubleVal=#{val}")
    int countForDoubleValue(@Param("table") String table, @Param("fieldName") String fieldName,
            @Param("val") Double value);
    
    @Select("SELECT distinct(stringVal) from ${table} where fieldName=#{fieldName}")
    List<String> selectDistinctStringVal(@Param("table") String table, @Param("fieldName") String fieldName);

    @Select("SELECT distinct(longVal) from ${table} where fieldName=#{fieldName}")
    List<Long> selectDistinctLongVal(@Param("table") String table, @Param("fieldName") String fieldName);

    @Select("SELECT distinct(doubleVal) from ${table} where fieldName=#{fieldName}")
    List<Double> selectDistinctDoubleVal(@Param("table") String table, @Param("fieldName") String fieldName);

    @Select("SELECT count(distinct(keyVal)) from ${table}")
    int count(@Param("table") String table);

    @Select("SELECT keyVal from ${table} where fieldName='CLASSNAME' order by keyVal limit #{startIndex}, #{endIndex}")
    List<String> findIDs(@Param("table") String table,  @Param("startIndex") int startIndex, @Param("endIndex") int endIndex);

    @Select("SELECT distinct(keyVal) from ${table} where keyVal like #{id} order by keyVal limit #{amount} ")
    List<String> findIdsLike(@Param("table") String tableName, @Param("id") String idsLikeString,
            @Param("amount") int resultAmount);

    @Transactional
    @Insert("replace into ${table} values (#{row.created}, #{row.keyVal},#{row.fieldName},#{row.doubleVal},#{row.longVal},#{row.stringVal})")
    @Options(flushCache = true)
    void update(@Param("table") String table, @Param("row") GenericRow row);

}
