package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Businessplan;
import com.nt.dao_Pfans.PFANS1000.PersonPlanTable;
import com.nt.dao_Pfans.PFANS1000.Pltab;
import com.nt.dao_Pfans.PFANS1000.Vo.ActualPL;
import com.nt.dao_Pfans.PFANS5000.LogManagement;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PltabMapper extends MyMapper<Pltab> {

    List<Pltab> getPltab(@Param("groupid") String groupid, @Param("year") String year, @Param("month") String month);

    //add-ws-5/6-添加按份金额
    List<Pltab> selectPlmoney(@Param("groupid") String groupid, @Param("year") String year, @Param("month") String month);

    //add-ws-5/6-添加按份金额
    @Select("select IFNULL(sum(TIME_START),0) as TIME_START  from logmanagement where PROJECT_ID=#{project_id}  and  CREATEBY = #{createby} ")
    List<LogManagement> getstarttimesum(@Param("project_id") String project_id, @Param("createby") String createby);


}
