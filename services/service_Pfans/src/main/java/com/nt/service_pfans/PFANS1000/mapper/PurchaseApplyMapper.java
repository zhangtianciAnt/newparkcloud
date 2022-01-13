package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.dao_Pfans.PFANS1000.PurchaseApply;
import com.nt.utils.MyMapper;

import java.util.List;

public interface PurchaseApplyMapper extends MyMapper<PurchaseApply> {
    List<PurchaseApply> selectPurchaseApply();

    //region   add  ml  220112  检索  from
    List<PurchaseApply> getpurchaseApplySearch(PurchaseApply purchaseApply);
    //endregion   add  ml  220112  检索  to
}
