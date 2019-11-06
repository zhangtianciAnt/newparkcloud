package com.nt.service_pfans.PFANS2000;

import com.nt.dao_Pfans.PFANS2000.Purchase;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PurchaseService {

    //查看
    List<Purchase> getPurchase(Purchase purchase)throws Exception;

    public Purchase One(String purchase_id)throws  Exception;

    //创建
    public void insert(Purchase purchase, TokenModel tokenModel)throws  Exception;

    //修改
    public void updatePurchase(Purchase purchase,TokenModel tokenModel)throws  Exception;


}
