package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.TrafficDetails;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrafficDetailsMapper extends MyMapper<TrafficDetails> {

    List<Double> getCount(@Param("Evectionid") String Evectionid, @Param("Currency") String Currency);
}
