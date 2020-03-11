package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.utils.MyMapper;

import java.util.List;

public interface BusinessMapper extends MyMapper<Business> {
    List<Business> getBuse();
}
