package com.nt.service_AOCHUAN.AOCHUAN3000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Registration;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface RegistrationMapper extends MyMapper<Registration> {

    //根据主键查询
    public Registration selectByRegId (@Param("id") String id);
    //存在check
    public Integer existCheck(@Param("id") String id);
    //删除
    void del(@Param("id") String id , @Param("modifyby") String modifyby);
}
