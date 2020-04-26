package com.nt.service_AOCHUAN.AOCHUAN7000.mapper;


import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;


public interface CreruleMapper extends MyMapper<Crerule> {

    void delCrerule(@Param("modifyby") String modifyby, @Param("crerule_id") String crerule_id);
}
