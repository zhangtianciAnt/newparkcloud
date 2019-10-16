package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.CasgiftApply;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface CasgiftApplyService {
    //获取
    List<CasgiftApply> getCasgiftapply() throws Exception;
    //创建
    void insertCasgiftapply(CasgiftApply casgiftapply, TokenModel tokenModel) throws  Exception;
}
