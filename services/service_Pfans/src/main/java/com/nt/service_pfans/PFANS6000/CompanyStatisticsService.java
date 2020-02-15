package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface CompanyStatisticsService {

    List<CompanyStatistics> getCosts(Coststatistics coststatistics) throws Exception;

    List<CompanyStatistics> getWorkTimes(Coststatistics coststatistics);

    List<CompanyStatistics> getWorkTimeInfos(Coststatistics coststatistics);
}
