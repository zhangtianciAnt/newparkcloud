package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.AbNormal;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

public interface AbNormalService {

    void insert(AbNormal abNormal, TokenModel tokenModel) throws Exception;

    //获取异常申请列表信息
    List<AbNormal> list(AbNormal abNormal) throws Exception;

    //查询家长会申请
    List<AbNormal> selectAbNormalParent(String userid) throws Exception;

    //查询短病假长度
    Double getSickleave(String userid) throws Exception;

    void upd(AbNormal abNormal, TokenModel tokenModel) throws Exception;

    //根据id获取数据
    AbNormal One(String abnormalid) throws Exception;

    //根据id获取数据
    Map<String,String> cklength(AbNormal abNormal) throws Exception;
}
