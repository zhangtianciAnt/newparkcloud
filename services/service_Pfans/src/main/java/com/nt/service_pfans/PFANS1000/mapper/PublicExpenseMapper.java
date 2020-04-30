package com.nt.service_pfans.PFANS1000.mapper;

import com.nt.dao_Pfans.PFANS1000.PublicExpense;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

public interface PublicExpenseMapper extends MyMapper<PublicExpense> {
    Integer getInvoiceNo(String reimbursementDate);
    List<PublicExpense> getpublicelist(@Param("code") String publicexpenseid);
}
