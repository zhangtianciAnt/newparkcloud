package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Induction;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface InductionMapper extends MyMapper<Induction>{
    List<Induction> selectInduction(@Param("owners") List<String> owners,@Param("thisyear") String thisyear,@Param("lastyear") String lastyear,
                                    @Param("thismouth") String thismouth,@Param("lastmouth") String lastmouth);
}
