package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;

import java.util.List;
import java.util.Map;

public interface CompanyStatisticsService {

    Map<String, Object> getCosts(Coststatistics coststatistics) throws Exception;

    Map<String, Object> getWorkTimes(Coststatistics coststatistics);

    Map<String, Object> getWorkerCounts(Coststatistics coststatistics);
}
