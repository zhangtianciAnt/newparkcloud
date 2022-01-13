package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.dao_Pfans.PFANS1000.Offshore;
import com.nt.utils.MyMapper;

import java.util.List;

public interface OffshoreMapper extends MyMapper<Offshore> {

    //region   add  ml  220112  检索  from
    List<Offshore> getOffshoreSearch(Offshore offshore);
    //endregion   add  ml  220112  检索  to

}
