package com.nt.service_AOCHUAN.AOCHUAN6000;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;

import java.util.List;

public interface ReimbursementService {

    //获取费用主表
    List<Reimbursement> getReimbursementList() throws Exception;
}
