package com.nt.service_pfans.PFANS8000.mapper;

import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface WorkingDayMapper extends MyMapper<WorkingDay> {

    void deletete(@Param("day") Date workingday);

    List<WorkingDay> getDataList(@Param("THIS_YEAR") String this_year, @Param("LAST_YEAR") String last_year);

    String getHoliday(@Param("year") int year,@Param("mouth") int mouth,@Param("day") int day);

    Integer getHolidayExceptWeekend(@Param("start") Date start,@Param("end") Date end);
}
