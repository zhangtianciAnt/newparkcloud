package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Base;
import com.nt.utils.dao.TokenModel;
import com.nt.dao_Pfans.PFANS2000.Giving;
import java.util.List;

public interface GivingService {

    void insert(String generation, TokenModel tokenModel)throws Exception;

    /**
     * 生成基数表
     * FJL
     * */
    void insertBase(String  givingid,TokenModel tokenModel)throws Exception;

    List<Base> getListtBase(Base base) throws Exception;

    List<Giving> getDataList(Giving giving) throws Exception;

}


