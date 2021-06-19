package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.DepartmentalInside;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalInsideBaseVo;
import com.nt.dao_Pfans.PFANS1000.Vo.StaffWorkMonthInfoVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentalInsideMapper extends MyMapper<DepartmentalInside> {

    List<DepartmentalInsideBaseVo> getBaseInfo(@Param("YEAR") String year);

    List<StaffWorkMonthInfoVo> getWorkInfo(@Param("LOG_DATE") String log_date, @Param("userList") List<String> userList,@Param("projectList") List<String> projectList);

}
