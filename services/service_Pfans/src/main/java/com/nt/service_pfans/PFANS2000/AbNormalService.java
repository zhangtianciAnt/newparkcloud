package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface AbNormalService {

    void insert(AbNormal abNormal, TokenModel tokenModel) throws Exception;

    //获取异常申请列表信息
    List<AbNormal> list(AbNormal abNormal) throws Exception;

    void upd(AbNormal abNormal, TokenModel tokenModel) throws Exception;

    //根据id获取数据
    AbNormal One(String abnormalid) throws Exception;
}
