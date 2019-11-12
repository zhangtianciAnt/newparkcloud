package com.nt.service_pfans.PFANS2000;

import com.nt.utils.dao.TokenModel;
import com.nt.dao_Pfans.PFANS2000.Giving;
import java.util.List;

public interface GivingService {

    void insert(Giving giving, TokenModel tokenModel)throws Exception;

    List<Giving> getDataList(Giving giving) throws Exception;

}


