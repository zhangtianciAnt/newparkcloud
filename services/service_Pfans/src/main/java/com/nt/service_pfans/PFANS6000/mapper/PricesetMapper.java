package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PricesetMapper extends MyMapper<Priceset> {
    List<Priceset> selectByYear(@Param("startTime") String startTime, @Param("endTime") String endTime);
    List<Priceset> gettlist();
}
