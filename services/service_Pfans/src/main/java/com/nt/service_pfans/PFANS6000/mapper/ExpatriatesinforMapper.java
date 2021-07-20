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

    @Select("select * from expatriatesinfor where YJEXITIME = date_add(curdate(), interval -1 day)")
    List<Expatriatesinfor> getExpYjExitime();

}
