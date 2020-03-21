package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.Evection;
import com.nt.utils.MyMapper;

public interface EvectionMapper extends MyMapper<Evection> {
    Integer getInvoiceNo(String reimbursementDate);
}