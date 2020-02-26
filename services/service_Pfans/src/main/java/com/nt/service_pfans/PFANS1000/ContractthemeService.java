package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Contracttheme;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ContractthemeService {

    //查看
    List<Contracttheme> get(Contracttheme contracttheme) throws Exception;

    public Contracttheme One(String communication_id) throws Exception;

    //创建
    public void insert(Contracttheme contracttheme, TokenModel tokenModel) throws Exception;

    //修改
    public void update(Contracttheme contracttheme, TokenModel tokenModel) throws Exception;

}
