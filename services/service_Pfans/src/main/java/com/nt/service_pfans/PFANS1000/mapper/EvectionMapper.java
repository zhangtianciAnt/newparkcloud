package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Evection;
import com.nt.utils.MyMapper;

import java.util.List;

public interface EvectionMapper extends MyMapper<Evection> {
    Integer getInvoiceNo(String reimbursementDate);

    //region   add  ml  220112  检索  from
    List<Evection> getSearch(Evection evection);
    //endregion   add  ml  220112  检索  to
}
