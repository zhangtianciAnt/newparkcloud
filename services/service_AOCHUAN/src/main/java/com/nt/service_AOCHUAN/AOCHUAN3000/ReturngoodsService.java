package com.nt.service_AOCHUAN.AOCHUAN3000;


import com.nt.dao_AOCHUAN.AOCHUAN3000.Returngoods;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ReturngoodsService {

    List<Returngoods> get(Returngoods returngoods) throws Exception;

    public void insert(Returngoods returngoods, TokenModel tokenModel)throws  Exception;

    public Returngoods One(String ids)throws  Exception;

    public TransportGood findPeo(String ids)throws  Exception;

    public void update(Returngoods returngoods, TokenModel tokenModel)throws  Exception;

    void delete(String id) throws Exception;

    List<Returngoods> getcheck(String contractno) throws Exception;




}
