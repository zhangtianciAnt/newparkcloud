package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.ExpenditureForecast;
import com.nt.dao_Pfans.PFANS5000.Projectsystem;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExpenditureForecastMapper extends MyMapper<ExpenditureForecast> {

    //更新或插入
    int insertOrUpdateBatch(@Param("entities") List<ExpenditureForecast> entities);

    //查询
    List<ExpenditureForecast> selectOldRevenueForecastList(@Param("deptId") String deptId, @Param("year") String year, @Param("saveDate") Date saveDate, @Param("themeName") String themeName);

    //获取未关联theme
    List<ExpenditureForecast> getThemeOutDepth(@Param("deptId") String deptId,@Param("year") int year,@Param("saveDate") Date saveDate);

    //获取关联theme
    List<ExpenditureForecast> getTheme(@Param("deptId") String deptId,@Param("year") int year);

    //返回theme，对应项目对应人的对应月份的总工数和金额之和,适用员工和构内
    List<Map<String,String>> getProjectsystem(@Param("deptId") String deptId, @Param("themeInforId") String themeInforId,
                                              @Param("logDate") String logDate, @Param("year") String year, @Param("type") String type);

    //获取应出勤总时长
    String forAttendance(@Param("logdate") String logdate);

    //获取对应theme，部门，年度，月份的构内金额之和
    String getPjExter(@Param("deptId") String deptId, @Param("year") String year,@Param("themeInforId") String themeInforId,@Param("logDate") String logDate);

    //获取对应theme，部门，年月的构外工数和金额之和
    List<Map<String,String>> outsideStructure(@Param("deptId") String deptId,@Param("themeInforId") String themeInforId,@Param("logDate") String logDate);

    //批量更新实际
    void updateList(@Param("list") List<ExpenditureForecast> expenditureForecastList);

    //获取全年员工计划工数
    ExpenditureForecast employeeWork(@Param("deptId") String deptId,@Param("year") String year,@Param("classIfication") String classIfication, @Param("saveDate") String saveDate);

    //从打卡获取全年度社内人员人数
    ExpenditureForecast employeesToClockIn(@Param("deptname") String deptname,@Param("year") String year);

}

