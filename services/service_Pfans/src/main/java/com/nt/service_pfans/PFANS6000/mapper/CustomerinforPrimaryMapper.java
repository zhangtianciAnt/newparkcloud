package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS1000.DepartmentAccountTotal;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.dao_Pfans.PFANS6000.CustomerinforPrimary;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CustomerinforPrimaryMapper extends MyMapper<CustomerinforPrimary> {

    void insertList(@Param("list") List<CustomerinforPrimary> customerinforPrimaryList);

}
