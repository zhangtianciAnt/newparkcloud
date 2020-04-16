package com.nt.service_AOCHUAN.AOCHUAN6000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;
import com.nt.service_AOCHUAN.AOCHUAN6000.ReimbursementService;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.ReimbursementMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReimburesmentServiceImpl implements ReimbursementService {

    @Autowired
    private ReimbursementMapper reimbursementMapper;

    //获取费用主表
    @Override
    public List<Reimbursement> getReimbursementList() throws Exception {
        return reimbursementMapper.getReimbursementList();
    }
}
