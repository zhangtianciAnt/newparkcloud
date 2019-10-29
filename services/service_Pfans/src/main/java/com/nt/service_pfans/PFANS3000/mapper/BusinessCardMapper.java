package com.nt.service_pfans.PFANS3000.mapper;

import com.nt.dao_Pfans.PFANS3000.BusinessCard;
import com.nt.utils.MyMapper;

import java.util.List;

public interface BusinessCardMapper extends MyMapper<BusinessCard> {
    List<BusinessCard> getBusinessCard();
}
