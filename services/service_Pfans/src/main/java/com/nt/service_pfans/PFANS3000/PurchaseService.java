package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface PurchaseService {

    List<Purchase> getPurchase(Purchase purchase)throws Exception;

    public Purchase One(String purchase_id)throws  Exception;

    public void insert(Purchase purchase, TokenModel tokenModel)throws  Exception;

    public void updatePurchase(Purchase purchase,TokenModel tokenModel)throws  Exception;

}
