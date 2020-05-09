package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.ThemePlanDetail;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ThemePlanDetailMapper extends MyMapper<ThemePlanDetail> {

    List<ThemePlanDetail> getDetailList(@Param("themeplan_id") String themeplan_id);

}
