package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.LoanApplication;
import com.nt.utils.MyMapper;

import java.util.List;

public interface LoanApplicationMapper  extends MyMapper<LoanApplication> {
    List<LoanApplication> getLoapp();

    Integer getLoappCount(String createon);
}
