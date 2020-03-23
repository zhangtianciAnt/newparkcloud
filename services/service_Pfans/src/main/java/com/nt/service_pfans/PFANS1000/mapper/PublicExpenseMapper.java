package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.utils.MyMapper;

import java.util.Date;

public interface PublicExpenseMapper extends MyMapper<PublicExpense> {
    Integer getInvoiceNo(String reimbursementDate);
}
