package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Base;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper extends MyMapper<Base> {
    int insertBase(@Param("bases") List<Base> bases);

    List<Base> selectBaseType();
}
