package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Fixedassets;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface FixedassetsService {


    //查看
    List<Fixedassets> getFixedassets(Fixedassets fixedassets)throws Exception;

    public Fixedassets One(String fixedassets_id)throws  Exception;

    //创建
    public void insert(Fixedassets fixedassets, TokenModel tokenModel)throws  Exception;

    //修改
    public void updateFixedassets(Fixedassets fixedassets,TokenModel tokenModel)throws  Exception;
}
