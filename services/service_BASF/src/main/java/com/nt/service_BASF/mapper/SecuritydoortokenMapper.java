package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Securitydoortoken;
import com.nt.dao_BASF.VO.SecuritydoortokenVo;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface SecuritydoortokenMapper extends MyMapper<Securitydoortoken> {

    SecuritydoortokenVo getDailyInfoToken(@Param("id") String id) throws Exception;
}
