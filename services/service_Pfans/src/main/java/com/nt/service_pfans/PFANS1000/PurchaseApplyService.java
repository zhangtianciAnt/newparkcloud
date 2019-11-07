package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Vo.PurchaseApplyVo;
import com.nt.utils.dao.TokenModel;

public interface PurchaseApplyService {

    //新建
    void insert(PurchaseApplyVo purchaseApplyVo, TokenModel tokenModel) throws Exception;

    //编辑
    void update(PurchaseApplyVo purchaseApplyVo, TokenModel tokenModel) throws Exception;

    //按id查询
    PurchaseApplyVo selectById(String purchaseApplyid) throws Exception;

}
