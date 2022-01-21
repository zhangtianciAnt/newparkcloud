package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Offshore;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface OffshoreService {

    List<Offshore> getOffshore(Offshore offshore)throws Exception;

    public Offshore One(String offshore_id)throws  Exception;

    public void insert(Offshore offshore, TokenModel tokenModel)throws  Exception;

    public void updateOffshore(Offshore offshore, TokenModel tokenModel)throws  Exception;

    //region   add  ml  220112  检索  from
    List<Offshore> getOffshoreSearch(Offshore offshore) throws Exception;
    //endregion   add  ml  220112  检索  to
}
