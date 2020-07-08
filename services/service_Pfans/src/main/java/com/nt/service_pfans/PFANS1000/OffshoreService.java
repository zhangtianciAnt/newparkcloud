package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Offshore;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface OffshoreService {

    List<Offshore> getOffshore(Offshore offshore)throws Exception;

    public Offshore One(String offshore_id)throws  Exception;

    public void insert(Offshore offshore, TokenModel tokenModel)throws  Exception;

    public void updateOffshore(Offshore offshore, TokenModel tokenModel)throws  Exception;

    //add-ws-7/7-禅道153
    List<Offshore> selectById3(String business_id) throws Exception;
    //add-ws-7/7-禅道153
}
