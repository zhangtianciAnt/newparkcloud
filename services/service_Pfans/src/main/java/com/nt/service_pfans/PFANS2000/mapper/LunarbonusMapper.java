package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import com.nt.dao_Pfans.PFANS2000.Vo.LunardetailVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface LunarbonusMapper extends MyMapper<Lunarbonus>{
    List<Lunarbonus> selectSee(@Param("lunarbonusList") List<String> lunarbonusList);
}

