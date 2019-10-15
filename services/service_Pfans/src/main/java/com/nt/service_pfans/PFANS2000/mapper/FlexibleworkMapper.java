package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Flexiblework;
import com.nt.utils.MyMapper;

import java.util.List;

public interface FlexibleworkMapper extends MyMapper<Flexiblework> {
      List<Flexiblework> getFlexiblework();
      //int insert(@Param("informationDelivery") InformationDelivery informationDelivery);
}
