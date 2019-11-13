package com.nt.service_pfans.PFANS8000.mapper;

import com.nt.dao_Pfans.PFANS8000.WorkingDay;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;



public interface WorkingDayMapper extends MyMapper<WorkingDay> {

    void deletete(@Param("day") Date workingday);

}
