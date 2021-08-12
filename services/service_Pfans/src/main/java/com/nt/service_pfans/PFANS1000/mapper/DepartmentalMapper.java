package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Contract;
import com.nt.dao_Pfans.PFANS1000.Departmental;
import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import com.nt.dao_Pfans.PFANS1000.Vo.DepartmentalVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DepartmentalMapper extends MyMapper<Departmental> {

    List<DepartmentalVo> getProConInfo(@Param("logdate") String nowDate);

    String getStaffInfo(@Param("departGroupFilter") String departGroupFilter,@Param("logdate") String nowDate);

    int saveStaffList(@Param("departmentalList") List<Departmental> departmentalList,@Param("ordernum") String ordernum);

}
