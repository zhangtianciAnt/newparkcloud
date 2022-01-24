package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BusinessMapper extends MyMapper<Business> {
    List<Business> getBuse();

    @Select("select * FROM business WHERE TO_DAYS( NOW( ) ) - TO_DAYS(date_format(  MODIFYON,'%Y-%m-%d' )) = 1 and `STATUS` = 4")
    List<Business> getBusList();

    //region   add  ml  220112  检索  from
    List<Business> getBusinessSearch(Business business);
    //endregion   add  ml  220112  检索  to

}
