package com.nt.service_AOCHUAN.AOCHUAN4000;


import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Sample;
import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import com.nt.dao_AOCHUAN.AOCHUAN4000.Products;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface ProductsService {

    List<Products> get(Products products) throws Exception;

    public void insert(Products offshore, TokenModel tokenModel)throws  Exception;

    public Products One(String ids)throws  Exception;

    public void update(Products products, TokenModel tokenModel)throws  Exception;

    void delete(String id) throws Exception;

    List<Supplierbaseinfor> getGYS(String ids) throws Exception;

    List<TransportGood> getZH(String ids) throws Exception;

    List<Sample> getYP(String ids) throws Exception;

    List<Quotations> getBJ(String ids) throws Exception;

    /**
     * @param product
     * @param tokenModel
     * @return id
     * @throws Exception
     * @author zhaoyoubing
     */
    public Products insertForSupplier(Products product, TokenModel tokenModel)throws  Exception;

    /**
     * 获取不在项目表中的数据
     * @return
     * @throws Exception
     */
    List<Products> getProdutsExceptUnique() throws Exception;
}
