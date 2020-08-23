package com.nt.service_pfans.PFANS8000.mapper;

import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface WorkingDayMapper extends MyMapper<WorkingDay> {

    void deletete(@Param("day") Date workingday);

    List<WorkingDay> getDataList(@Param("THIS_YEAR") String this_year, @Param("LAST_YEAR") String last_year);

    List<WorkingDay> getworkingyear(@Param("start") String start, @Param("end") String end);

    String getHoliday(@Param("year") int year,@Param("mouth") int mouth,@Param("day") int day);

    Integer getHolidayExceptWeekend(@Param("start") Date start,@Param("end") Date end);

    //ccm 1019 计算一个月出勤多少小时
    List<WorkingDay> getBymonth(@Param("startDate") String startDate,@Param("endDate") String endDate, @Param("year") String year);
    //ccm 1019 计算一个月出勤多少小时

    //gbb 1022 获取条件时间段内的工作日
    List<WorkingDay> getWorkingday(@Param("startDate") String startDate,@Param("endDate") String endDate);
    //gbb 1022 获取条件时间段内的工作日
}
