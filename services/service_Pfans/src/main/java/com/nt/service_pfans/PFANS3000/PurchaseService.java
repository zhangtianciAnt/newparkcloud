package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.utils.dao.TokenModel;

import java.util.List;
import java.util.Map;

public interface PurchaseService {

    List<Purchase> getPurchase(Purchase purchase, TokenModel tokenModel)throws Exception;

    List<Purchase> getPurchaselist(Purchase purchase)throws Exception;

    public Purchase One(String purchase_id)throws  Exception;

    public void insert(Purchase purchase, TokenModel tokenModel)throws  Exception;

    public void updatePurchase(Purchase purchase,TokenModel tokenModel)throws  Exception;

    //采购业务数据流程查看详情
    public Map<String, String> getworkfolwPurchaseData(Purchase purchase) throws Exception;
    //采购业务数据流程查看详情
}
