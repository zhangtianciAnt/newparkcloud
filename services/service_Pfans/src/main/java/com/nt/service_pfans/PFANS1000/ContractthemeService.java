package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Contracttheme;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ContractthemeService {

    //查看
    List<Contracttheme> get(Contracttheme contracttheme) throws Exception;

    //创建
    public void insert(List<Contracttheme> contracttheme, TokenModel tokenModel) throws Exception;

}
