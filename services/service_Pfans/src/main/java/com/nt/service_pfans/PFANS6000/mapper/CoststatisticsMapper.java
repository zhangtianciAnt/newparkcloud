package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CoststatisticsMapper extends MyMapper<Coststatistics> {

    List<Coststatistics> getExpatriatesinfor(@Param("year") int year,@Param("groupid") String groupid);
    List<Coststatistics> selectBygroupid(@Param("groupid") String groupid);
    List<Coststatistics> getCoststatisticsBygroupid(@Param("year") int year,@Param("groupid") String groupid);
    int insertAll(List<Coststatistics> allCostList);
    List<Map<String, String>> getcostMonth(@Param("manhour") String manhour,@Param("cost") String cost,@Param("groupid") String groupid);
}

