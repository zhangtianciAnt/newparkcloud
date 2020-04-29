package com.nt.service_AOCHUAN.AOCHUAN7000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface AuxiliaryAccountingItemsMapper extends MyMapper<Helprule> {

    //删除Helprule
    void delHelpruleid(@Param("modifyby") String modifyby, @Param("crerule_wid") String crerule_wid);

    //存在Check
    int existCheckAch(@Param("id") String id);
}
