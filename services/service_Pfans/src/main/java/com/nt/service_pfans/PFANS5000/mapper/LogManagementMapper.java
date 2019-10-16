package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogManagementMapper extends MyMapper<LogManagement> {

    List<LogManagement> getDataList(@Param("THIS_YEAR") String this_year, @Param("LAST_YEAR") String last_year);
}
