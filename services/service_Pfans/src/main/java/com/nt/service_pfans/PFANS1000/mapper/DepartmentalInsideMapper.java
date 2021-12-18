package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.DepartmentalInside;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideBaseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.StaffWorkMonthInfoVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentalInsideMapper extends MyMapper<DepartmentalInside> {

    List<DepartmentalInsideBaseVo> getBaseInfo(@Param("YEAR") String year);

    List<StaffWorkMonthInfoVo> getWorkInfo(@Param("LOG_DATE") String log_date, @Param("departList") List<String> departList,@Param("projectList") List<String> projectList);

    //部门别表批量插入数据
    void insertDepInsAll(@Param("list") List<DepartmentalInside> departmentalInsideListInsert);
    //部门别表批量更新数据
    void updateDepInsAll(@Param("list") List<DepartmentalInside> departmentalInsideListUnpdate);
}
