package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AbNormalService {
    public void insert(AbNormal abNormal, TokenModel tokenModel)throws Exception;

    //获取所有审批流程
    public List<AbNormal> list(AbNormal abNormal) throws Exception;
}
