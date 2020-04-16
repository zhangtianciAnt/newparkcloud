package com.nt.service_AOCHUAN.AOCHUAN4000;


import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ProductsService {

    List<Products> get(Products products) throws Exception;

    public void insert(Products offshore, TokenModel tokenModel)throws  Exception;

    public Products One(String ids)throws  Exception;

    public void update(Products products, TokenModel tokenModel)throws  Exception;

    void delete(String id) throws Exception;

    /**
     * @param product
     * @param tokenModel
     * @return id
     * @throws Exception
     * @author zhaoyoubing
     */
    public String insertForSupplier(Products product, TokenModel tokenModel)throws  Exception;


}
