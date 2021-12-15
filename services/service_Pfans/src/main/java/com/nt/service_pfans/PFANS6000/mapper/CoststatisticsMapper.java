package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Coststatistics;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CoststatisticsMapper extends MyMapper<Coststatistics> {

    List<Coststatistics> getExpatriatesinfor(@Param("year") int year,@Param("groupid") String groupid);
    List<Coststatistics> selectBygroupid(@Param("groupid") String groupid,@Param("year") String year);
    List<Coststatistics> getCoststatisticsBygroupid(@Param("year") int year,@Param("groupid") String groupid);

    int insertAll(@Param("list")  List<Coststatistics> allCostList);
    int updateAll(@Param("list")  List<Coststatistics> allCostList);

    //gbb add 0804 月度赏与列表
    List<Coststatistics> getcostMonthList(@Param("years") String years,@Param("groupIdList")List<String> groupIdList);
    //gbb add 0804 月度赏与详情
    List<Map<String, String>> getcostMonth(@Param("years") String years,@Param("manhour") String manhour,@Param("cost") String cost
            ,@Param("expensesolo") String expensesolo,@Param("months") String months,@Param("groupid") String groupid
            ,@Param("manhourf") String manhourf,@Param("costf") String costf);
    List<Workflowinstance> getworkflowinstance(@Param("groupIdList")List<String> groupIdList);
    //gbb add 0914 查询经费
    List<Coststatistics> getCoststatisticsExpense(@Param("years") String years,@Param("groupid") String groupid);
}

