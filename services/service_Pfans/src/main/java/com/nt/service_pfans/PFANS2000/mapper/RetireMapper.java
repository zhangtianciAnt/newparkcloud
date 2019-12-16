package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Retire;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RetireMapper extends MyMapper<Retire> {
    List<Retire> selectRetire(@Param("owners") List<String> owners, @Param("thisyear") String thisyear, @Param("thismouth") String thismouth);
}
