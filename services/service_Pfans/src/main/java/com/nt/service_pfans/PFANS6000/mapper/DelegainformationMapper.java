package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Delegainformation;
import com.nt.dao_Pfans.PFANS6000.Vo.DelegainformationVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DelegainformationMapper extends MyMapper<Delegainformation> {
   List<DelegainformationVo> getYears(@Param("year") String year,@Param("owners") List<String> owners);

}
