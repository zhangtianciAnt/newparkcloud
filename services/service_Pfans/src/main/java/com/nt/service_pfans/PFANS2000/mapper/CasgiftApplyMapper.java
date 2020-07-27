package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;


public interface CasgiftApplyMapper extends MyMapper<CasgiftApply>{
    void updpayment(@Param("releasedate") String releasedate,@Param("user_id") String user_id);
}
