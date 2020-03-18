package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Accumulatedtax;
import com.nt.dao_Pfans.PFANS2000.Vo.AccumulatedTaxVo;
import com.nt.utils.MyMapper;

import java.util.List;

public interface AccumulatedTaxMapper extends MyMapper<Accumulatedtax> {
    List<AccumulatedTaxVo> getaccumulatedTax();
}
