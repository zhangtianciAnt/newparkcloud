package com.nt.service_pfans.PFANS3000.mapper;

import com.nt.dao_Pfans.PFANS1000.Offshore;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PurchaseMapper  extends MyMapper<Purchase> {
    @Select("select * from Purchase")
    List<Purchase> getPurchaseEnd();

    //region   add  ml  220112  检索  from
    List<Purchase> getPurchaseSearch(Purchase purchase);
    //endregion   add  ml  220112  检索  to
}
