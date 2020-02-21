package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.utils.MyMapper;

import java.util.List;

public interface CoststatisticsMapper extends MyMapper<Coststatistics> {

    List<Coststatistics> getExpatriatesinfor(Coststatistics param);

    int insertAll(List<Coststatistics> allCostList);
}

