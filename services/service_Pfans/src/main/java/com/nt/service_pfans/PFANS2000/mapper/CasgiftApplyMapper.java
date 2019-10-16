package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CasgiftApplyMapper extends MyMapper<CasgiftApply>{
    List<CasgiftApply> getCasgiftApply();
    int insert(@Param("casgiftapply") CasgiftApply casgiftapply);
    int updateSelective(@Param("casgiftapply") CasgiftApply casgiftapply);

}
