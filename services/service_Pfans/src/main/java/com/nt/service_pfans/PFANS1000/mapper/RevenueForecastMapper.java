package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.RevenueForecast;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * Theme别收入见通(RevenueForecast)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-18 14:58:34
 */
public interface RevenueForecastMapper extends MyMapper<RevenueForecast> {

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RevenueForecast> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<RevenueForecast> entities);

    List<RevenueForecast> selectRevenueForecastListFirst(@Param("deptId") String deptId,@Param("year") int year,@Param("saveDate") Date saveDate);

    List<RevenueForecast> selectOldRevenueForecastList(@Param("deptId") String deptId,@Param("year") int year,@Param("saveDate") Date saveDate);

    List<RevenueForecast> selectRevenueForecastList(@Param("deptId") String deptId,@Param("year") int year,@Param("saveDate") Date saveDate);

    List<RevenueForecast> getThemeOutDepth(@Param("deptId") String deptId,@Param("year") int year,@Param("saveDate") Date saveDate);

}

