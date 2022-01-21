package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface ExpatriatesinforMapper extends MyMapper<Expatriatesinfor> {
    @Select("select * from Expatriatesinfor")
    List<Expatriatesinfor> getExpatriatesinforexit();

    @Select(" SELECT EXPATRIATESINFOR_ID, WHETHERENTRY FROM expatriatesinfor WHERE Whetherentry = 'BP006001'")
    List<String> getPeolple1();

    //    dialog优化分页 ztc fr
    @Select(" SELECT * FROM expatriatesinfor WHERE NUMBER != '00000'")
    List<Expatriatesinfor> getOutList();
    //    dialog优化分页 ztc to

    //region   add  ml  220112  检索  from
    List<Expatriatesinfor> getSearch(Expatriatesinfor expatriatesinfor);
    //endregion   add  ml  220112  检索  to


}
