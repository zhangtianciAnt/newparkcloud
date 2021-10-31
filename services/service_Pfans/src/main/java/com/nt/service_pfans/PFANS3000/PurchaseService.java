package com.nt.service_pfans.PFANS3000;

import com.nt.dao_Pfans.PFANS2000.Staffexitprocedure;
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

    void change(Purchase purchase, TokenModel tokenModel) throws Exception;

    //region scc add 10/28 购买决裁决裁逻辑删除 from
    void purchdelete(Purchase purchase, TokenModel tokenModel) throws Exception;
    //endregion scc add 10/28 购买决裁决裁逻辑删除 to
}
