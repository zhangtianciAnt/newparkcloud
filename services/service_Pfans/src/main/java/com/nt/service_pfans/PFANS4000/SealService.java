package com.nt.service_pfans.PFANS4000;

import com.nt.dao_Pfans.PFANS4000.Seal;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface SealService {

    void insert(Seal seal, TokenModel tokenModel) throws Exception;

    //获取异常申请列表信息
    List<Seal> list(Seal seal) throws Exception;

    void upd(Seal seal, TokenModel tokenModel) throws Exception;

    //根据id获取数据
    Seal One(String sealid) throws Exception;
}
