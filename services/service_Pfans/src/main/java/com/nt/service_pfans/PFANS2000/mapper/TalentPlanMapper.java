package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.TalentPlan;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TalentPlanMapper extends MyMapper<TalentPlan> {
    List<TalentPlan> selectByuserId(@Param("userIdList")List<String> userIdList);
}
