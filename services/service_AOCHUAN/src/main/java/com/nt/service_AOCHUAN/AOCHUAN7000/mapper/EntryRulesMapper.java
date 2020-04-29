package com.nt.service_AOCHUAN.AOCHUAN7000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface EntryRulesMapper extends MyMapper<Crerule> {

    //删除Docurule
    void delCreruleid(@Param("modifyby") String modifyby, @Param("docurule_fid_id") String docurule_fid_id);

    //存在Check
    int existCheckAcc(@Param("id") String id);
}
