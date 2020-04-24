package com.nt.service_AOCHUAN.AOCHUAN5000;

import com.nt.dao_AOCHUAN.AOCHUAN5000.FinPurchase;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface FinPurchaseSerivce {

    //获取财务-采购数据
    List<FinPurchase> getFinPurchaseList(FinPurchase finPurchase) throws  Exception;

    FinPurchase getForm(String id) throws Exception;

    //更新
    void update(FinPurchase finSales, TokenModel tokenModel) throws Exception;

    //存在Check
    Boolean existCheck(FinPurchase finPurchase) throws Exception;

    //唯一性Check
    Boolean uniqueCheck(FinPurchase finPurchase) throws Exception;
}
