package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Casgiftapply;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface CasgiftapplyService {
    //获取
    List<Casgiftapply> getCasgiftapply() throws Exception;
    //创建
    void insertCasgiftapply(Casgiftapply casgiftapply, TokenModel tokenModel) throws  Exception;
}
