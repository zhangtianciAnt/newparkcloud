package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CustomerinforMapper extends MyMapper<Customerinfor> {
    void updateCustAll(@Param("list") List<Customerinfor> customerinforList);

    List<Customerinfor> export(@Param("list") List<String> ids);

}
