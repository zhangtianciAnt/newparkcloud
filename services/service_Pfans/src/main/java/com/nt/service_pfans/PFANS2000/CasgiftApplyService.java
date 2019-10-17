package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface CasgiftApplyService {

    //查看
    List<CasgiftApply> getCasgiftApply(CasgiftApply casgiftapply) throws Exception;

    //创建
    void insertCasgiftApply(CasgiftApply casgiftapply, TokenModel tokenModel) throws Exception;

    //修改
    void updateCasgiftApply(CasgiftApply casgiftapply, TokenModel tokenModel) throws Exception;

}
