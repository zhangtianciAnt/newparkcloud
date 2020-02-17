package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface CoststatisticsService {
    List<Coststatistics> getCostList(Coststatistics coststatistics) throws Exception;

    Integer insertCoststatistics(Coststatistics coststatistics, TokenModel tokenModel) throws Exception;
}
