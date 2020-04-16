package com.nt.service_AOCHUAN.AOCHUAN6000.mapper;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;
import com.nt.utils.MyMapper;

import java.util.List;

public interface ReimbursementMapper extends MyMapper<Reimbursement> {

    //获取费用主表
    public List<Reimbursement> getReimbursementList();
}
