package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Base;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper extends MyMapper<Base> {
    int insertBase(@Param("bases") List<Base> bases);

    //获取基数 type -lxx
    List<Base> selectBaseType(@Param("givingid") String givingid);
}
