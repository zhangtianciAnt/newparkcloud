package com.nt.service_pfans.PFANS4000.mapper;

import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PeoplewareFeeMapper extends MyMapper<PeoplewareFee> {
    void insertList(@Param("fees") List<PeoplewareFee> peoplewareFees);

    void updateFeeList(@Param("list") List<PeoplewareFee> peoplewareFees);
}
