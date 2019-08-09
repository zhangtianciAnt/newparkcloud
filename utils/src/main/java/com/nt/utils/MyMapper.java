package com.nt.utils;


import java.util.List;

import org.apache.ibatis.annotations.CacheNamespaceRef;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.session.RowBounds;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.base.BaseDeleteMapper;
import tk.mybatis.mapper.common.base.select.SelectAllMapper;
import tk.mybatis.mapper.common.base.select.SelectCountMapper;
import tk.mybatis.mapper.common.base.select.SelectOneMapper;
import tk.mybatis.mapper.provider.base.BaseInsertProvider;
import tk.mybatis.mapper.provider.base.BaseSelectProvider;
import tk.mybatis.mapper.provider.base.BaseUpdateProvider;

public interface MyMapper<T> extends
        SelectOneMapper<T>,
        SelectAllMapper<T>,
        SelectCountMapper<T>,
        BaseDeleteMapper<T>{
    //static final String CACHE_KEY = "''";

    //@Cacheable(value="myehcache", keyGenerator = "CacheKeyGenerator")
    @SelectProvider(type = SelectWithAuth.class, method = "dynamicSQL")
    List<T> selectByRowBounds(T record, RowBounds rowBounds);

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param record
     * @return
     */
    //@Cacheable(value="myehcache", keyGenerator = "CacheKeyGenerator")
    @SelectProvider(type = SelectWithAuth.class, method = "dynamicSQL")
    List<T> select(T record);
    /**
     * 根据主键字段进行查询，方法参数必须包含完整的主键属性，查询条件使用等号
     *
     * @param key
     * @return
     */
    //@Cacheable(value="myehcache", key="#p0")
    @SelectProvider(type = SelectWithAuth.class, method = "dynamicSQL")
    T selectByPrimaryKey(Object key);
    /**
     * 保存一个实体，null的属性也会保存，不会使用数据库默认值
     *
     * @param record
     * @return
     */
    //@CacheEvict(value="myehcache", allEntries=true)
    @InsertProvider(type = SelectWithAuth.class, method = "dynamicSQL")
    int insert(T record);
    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param record
     * @return
     */
    //@CacheEvict(value="myehcache", allEntries=true)
    @InsertProvider(type = SelectWithAuth.class, method = "dynamicSQL")
    int insertSelective(T record);
    /**
     * 根据主键更新属性不为null的值
     *
     * @param record
     * @return
     */
    //@CacheEvict(value="myehcache", allEntries=true)
    @UpdateProvider(type = SelectWithAuth.class, method = "dynamicSQL")
    //@Options(useCache = false, useGeneratedKeys = false)
    int updateByPrimaryKeySelective(T record);
    /**
     * 根据主键更新实体全部字段，null值会被更新
     *
     * @param record
     * @return
     */
    //@CacheEvict(value="myehcache", allEntries=true)
    @UpdateProvider(type = SelectWithAuth.class, method = "dynamicSQL")
    //@Options(useCache = false, useGeneratedKeys = false)
    int updateByPrimaryKey(T record);
}
