package com.nt.service_AOCHUAN.AOCHUANMENHU;


import com.nt.dao_AOCHUAN.AOCHUANMENHU.Menhuproducts;
import com.nt.dao_AOCHUAN.AOCHUANMENHU.Newsinformation;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface MenhunewsService {

    List<Newsinformation> get(Newsinformation newsinformation) throws Exception;

    public void insert(Newsinformation newsinformation, TokenModel tokenModel)throws  Exception;

    public Newsinformation One(String ids)throws  Exception;

    void delete(String id) throws Exception;

    public void update(Newsinformation newsinformation, TokenModel tokenModel)throws  Exception;


}
