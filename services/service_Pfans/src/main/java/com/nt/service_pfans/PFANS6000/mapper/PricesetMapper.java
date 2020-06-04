package com.nt.service_pfans.PFANS6000.mapper;

import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.ExpatriatesinforDetail;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PricesetMapper extends MyMapper<Priceset> {
    List<Priceset> selectByYear(@Param("year") String year);
    List<Priceset> gettlist();
    List<Priceset> selectBygroupid(@Param("years") int years,@Param("groupid") String groupid);
}
