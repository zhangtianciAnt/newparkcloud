package com.nt.service_AOCHUAN.AOCHUAN7000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface VoucherRulesMapper extends MyMapper<Docurule> {

    //删除Docurule
    void delDocuruleid(@Param("modifyby") String modifyby, @Param("docurule_id") String docurule_id);

    //存在Check
    int existCheckAux(@Param("id") String id);

}
