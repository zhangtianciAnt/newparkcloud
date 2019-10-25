package com.nt.service_pfans.PFANS8000.mapper;

import com.nt.dao_Pfans.PFANS8000.InformationDelivery;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InformationDeliveryMapper extends MyMapper<InformationDelivery> {
      List<InformationDelivery> getInformation(@Param("owners") List<String> owners);
}
