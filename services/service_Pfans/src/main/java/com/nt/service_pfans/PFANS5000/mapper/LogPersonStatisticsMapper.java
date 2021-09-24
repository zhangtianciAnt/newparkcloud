package com.nt.service_pfans.PFANS5000.mapper;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.dao_Pfans.PFANS5000.LogPersonStatistics;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogPersonStatisticsMapper extends MyMapper<LogPersonStatistics> {

    List<LogPersonStatistics> selectByDate(@Param("owners") List<String> owners,@Param("dateOf") String dateOf);

    List<LogPersonStatistics> selectByDateLogment(@Param("years") String years);
}
