package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Security;
import com.nt.dao_Pfans.PFANS1000.Vo.SecurityVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface SecurityService {

    List<Security> getSecurity(Security security) throws Exception;

    public SecurityVo selectById(String securityid) throws Exception;

    void updateSecurity(SecurityVo securityVo, TokenModel tokenModel) throws Exception;

    public void insert(SecurityVo securityVo, TokenModel tokenModel)throws Exception;

}
