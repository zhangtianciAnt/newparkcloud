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
    //gbb add 0804 月度赏与列表
    List<Coststatistics> getcostMonthList(@Param("years") String years,@Param("groupIdList")List<String> groupIdList);
    //gbb add 0804 月度赏与详情
    List<Map<String, String>> getcostMonth(@Param("years") String years,@Param("manhour") String manhour,@Param("cost") String cost
            ,@Param("expense") String expense,@Param("months") String months,@Param("groupid") String groupid);
}

