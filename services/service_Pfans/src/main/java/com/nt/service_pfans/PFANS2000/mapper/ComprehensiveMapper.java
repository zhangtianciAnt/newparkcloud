package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Comprehensive;
import com.nt.dao_Pfans.PFANS2000.Vo.ComprehensiveVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ComprehensiveMapper extends MyMapper<Comprehensive> {
    List<ComprehensiveVo> getcomprehensive(@Param("givingid") String givingid, @Param("strMonths") String strMonths);
}
