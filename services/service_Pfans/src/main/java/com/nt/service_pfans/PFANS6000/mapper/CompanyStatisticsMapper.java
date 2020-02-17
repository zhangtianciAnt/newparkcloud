package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.utils.MyMapper;

import java.util.List;
import java.util.Map;

public interface CompanyStatisticsMapper extends MyMapper<CompanyStatistics> {

    List<CompanyStatistics> getWorkTimes(Map<String, Object> coststatistics);

    List<CompanyStatistics> getWorkers(Map<String, Object> coststatistics);
}

